package com.jeanpigomez.codingtest.ui.language;

import com.jeanpigomez.codingtest.model.Language;
import com.jeanpigomez.codingtest.network.repository.LanguageRepository;

import io.reactivex.Flowable;
import io.reactivex.schedulers.TestScheduler;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@RunWith(Parameterized.class)

public class LanguagePresenterTest {
    private static final Language LANGUAGE1 = new Language("Php", 16, "2018-12-5T05:30:00.000+0000");
    private static final Language LANGUAGE2 = new Language("Node", 20, "2018-12-18T05:30:00.000+0000");
    private static final Language LANGUAGE3 = new Language("Java", 12, "2019-02-27T20:30:00.000+0000");
    private static final List<Language> NO_LANGUAGE = Collections.emptyList();
    private static final List<Language> THREE_LANGUAGES =
            Arrays.asList(LANGUAGE1, LANGUAGE2, LANGUAGE3);

    @Mock
    private LanguageRepository repository;

    @Mock
    private LanguageContract.View view;


    @Parameters public static Object[] data() {
        return new Object[] { NO_LANGUAGE, THREE_LANGUAGES };
    }

    @Parameter public List<Language> languages;

    private TestScheduler testScheduler;

    private LanguagePresenter presenter;

    @Before public void setUp() {
        MockitoAnnotations.initMocks(this);
        testScheduler = new TestScheduler();
        presenter = new LanguagePresenter(repository, view, testScheduler, testScheduler);
    }

    @Test public void loadLanguages_ShouldAlwaysStopLoadingIndicatorOnView_WhenComplete() {
        // Given
        given(repository.loadLanguages(true)).willReturn(Flowable.just(languages));

        // When
        presenter.loadLanguages(true);
        testScheduler.triggerActions();

        // Then
        then(view).should(atLeastOnce()).stopLoadingIndicator();
    }

    @Test public void loadLanguages_ShouldShowLanguageOnView_WithDataReturned() {
        // Given
        given(repository.loadLanguages(true)).willReturn(Flowable.just(THREE_LANGUAGES));

        // When
        presenter.loadLanguages(true);
        testScheduler.triggerActions();

        // Then
        then(view).should().showLanguages(THREE_LANGUAGES);
        then(view).should(atLeastOnce()).stopLoadingIndicator();
    }

    @Test public void loadLanguages_ShouldShowMessage_WhenNoDataReturned() {
        // Given
        given(repository.loadLanguages(true)).willReturn(Flowable.just(NO_LANGUAGE));

        // When
        presenter.loadLanguages(true);
        testScheduler.triggerActions();

        // Then
        then(view).should(never()).showLanguages(any());
        then(view).should().showNoDataMessage();
        then(view).should(atLeastOnce()).stopLoadingIndicator();
    }

    @Test public void sort_ResultShouldBeShownOnView_WhenSortedByName() {
        // Given
        given(repository.loadLanguages(false)).willReturn(Flowable.just(THREE_LANGUAGES));

        // When
        presenter.sort(0);
        testScheduler.triggerActions();

        // Then
        // Return a list of questions sorted by name.
        then(view).should().showLanguages(Arrays.asList(LANGUAGE3, LANGUAGE2, LANGUAGE1));
        then(view).shouldHaveNoMoreInteractions();
    }

    @Test public void sort_ResultShouldBeShownOnView_WhenSortedByScore() {
        // Given
        given(repository.loadLanguages(false)).willReturn(Flowable.just(THREE_LANGUAGES));

        // When
        presenter.sort(1);
        testScheduler.triggerActions();

        // Then
        // Return a list of questions sorted by score.
        then(view).should().showLanguages(Arrays.asList(LANGUAGE3, LANGUAGE1, LANGUAGE2));
        then(view).shouldHaveNoMoreInteractions();
    }

    @Test public void sort_ResultShouldBeShownOnView_WhenSortedByEndDate() {
        // Given
        given(repository.loadLanguages(false)).willReturn(Flowable.just(THREE_LANGUAGES));

        // When
        presenter.sort(2);
        testScheduler.triggerActions();

        // Then
        // Return a list of questions sorted by endDate.
        then(view).should().showLanguages(Arrays.asList(LANGUAGE2, LANGUAGE1, LANGUAGE3));
        then(view).shouldHaveNoMoreInteractions();
    }
}