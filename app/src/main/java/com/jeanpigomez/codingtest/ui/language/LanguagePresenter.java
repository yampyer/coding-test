package com.jeanpigomez.codingtest.ui.language;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.res.Resources;

import com.jeanpigomez.codingtest.R;
import com.jeanpigomez.codingtest.model.Language;
import com.jeanpigomez.codingtest.network.repository.LanguageRepository;
import com.jeanpigomez.codingtest.utils.schedulers.RunOn;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.jeanpigomez.codingtest.utils.schedulers.SchedulerType.IO;
import static com.jeanpigomez.codingtest.utils.schedulers.SchedulerType.UI;

/**
 * A presenter with life-cycle aware.
 */
public class LanguagePresenter implements LanguageContract.Presenter, LifecycleObserver {

    private LanguageRepository repository;

    private LanguageContract.View view;

    private Scheduler ioScheduler;
    private Scheduler uiScheduler;

    private CompositeDisposable disposeBag;

    @Inject
    public LanguagePresenter(LanguageRepository repository, LanguageContract.View view,
                             @RunOn(IO) Scheduler ioScheduler, @RunOn(UI) Scheduler uiScheduler) {
        this.repository = repository;
        this.view = view;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;

        // Initialize this presenter as a lifecycle-aware when a view is a lifecycle owner.
        if (view instanceof LifecycleOwner) {
            ((LifecycleOwner) view).getLifecycle().addObserver(this);
        }

        disposeBag = new CompositeDisposable();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME) public void onAttach() {
        loadLanguages(false);
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE) public void onDetach() {
        // Clean up any no-longer-use resources here
        disposeBag.clear();
    }

    @Override
    public void addLanguage(String name, int score) {
        Language language = new Language(name, score, new Date().toString());
        repository.addLanguage(language);
        loadLanguages(false);
    }

    @Override
    public void loadLanguages(boolean onlineRequired) {

        // Load new one and populate it into view
        Disposable disposable = repository.loadLanguages(onlineRequired)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(this::handleReturnedData, this::handleError, () -> view.stopLoadingIndicator());
        disposeBag.add(disposable);
    }

    @Override
    public void sort(final int sortOption) {
        Disposable disposable = repository.loadLanguages(false)
                .flatMapIterable(languages -> languages)
                .toSortedList(sortOption == 0 ? (l1,l2) -> l1.getName().compareTo(l2.getName()) :
                sortOption == 1 ? (l1,l2) -> l1.getScore() - (l2.getScore()) : (l1,l2) -> l1.getEndDate().compareTo(l2.getEndDate()))
                .toFlowable()
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe(languages -> {
                    if (languages.isEmpty()) {
                        // Clear old data in view
                        view.clearLanguages();
                        // Show notification
                        view.showErrorMessage(Resources.getSystem().getString(R.string.sort_error));
                    } else {
                        // Update filtered data
                        view.showLanguages(languages);
                    }
                });

        disposeBag.add(disposable);
    }

    /**
     * Updates view after loading data is completed successfully.
     */
    private void handleReturnedData(List<Language> list) {
        view.stopLoadingIndicator();
        if (list != null && !list.isEmpty()) {
            view.showLanguages(list);
        } else {
            view.showNoDataMessage();
        }
    }

    /**
     * Updates view if there is an error after loading data from repository.
     */
    private void handleError(Throwable error) {
        view.stopLoadingIndicator();
        view.showErrorMessage(error.getLocalizedMessage());
    }
}
