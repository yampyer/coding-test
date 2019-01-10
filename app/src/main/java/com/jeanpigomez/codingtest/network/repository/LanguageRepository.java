package com.jeanpigomez.codingtest.network.repository;

import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.jeanpigomez.codingtest.model.Language;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LanguageRepository implements LanguageDataSource {

    private LanguageDataSource remoteDataSource;
    private LanguageDataSource localDataSource;

    @VisibleForTesting
    List<Language> caches;

    @Inject
    public LanguageRepository(@Local LanguageDataSource localDataSource,
                              @Remote LanguageDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;

        caches = new ArrayList<>();
    }

    @Override
    public Flowable<List<Language>> loadLanguages(boolean forceRemote) {
        if (forceRemote) {
            return refreshData();
        } else {
            // else return data from local storage
            return localDataSource.loadLanguages(false)
                    .take(1)
                    .flatMap(Flowable::fromIterable)
                    .doOnNext(language -> caches.add(language))
                    .toList()
                    .toFlowable()
                    .filter(list -> !list.isEmpty())
                    .switchIfEmpty(
                            refreshData()); // If local data is empty, fetch from remote source instead.
        }
    }

    /**
     * Fetches data from remote source.
     * Save it into both local database and cache.
     *
     * @return the Flowable newly fetched data.
     */
    Flowable<List<Language>> refreshData() {
        return remoteDataSource.loadLanguages(true).doOnNext(list -> {
            // Clear cache
            caches.clear();
            // Clear data in local storage
            localDataSource.clearData();
        }).flatMap(Flowable::fromIterable).doOnNext(language -> {
            caches.add(language);
            localDataSource.addLanguage(language);
        }).toList().toFlowable();
    }

    @Override
    public void addLanguage(Language language) {
        new AddLanguage().execute(language);
    }

    private class AddLanguage extends AsyncTask<Language, Integer, Long> {
        protected Long doInBackground(Language... languages) {
            caches.add(languages[0]);
            localDataSource.addLanguage(languages[0]);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
        }
    }

    @Override
    public void clearData() {
        caches.clear();
        localDataSource.clearData();
    }
}
