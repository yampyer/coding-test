package com.jeanpigomez.codingtest.network.repository.remote;

import com.jeanpigomez.codingtest.model.Language;
import com.jeanpigomez.codingtest.network.LanguageApi;
import com.jeanpigomez.codingtest.network.repository.LanguageDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LanguageRemoteDataSource implements LanguageDataSource {
    private LanguageApi languageApi;

    @Inject
    public LanguageRemoteDataSource(LanguageApi languageApi) {
        this.languageApi = languageApi;
    }

    @Override
    public Flowable<List<Language>> loadLanguages(boolean forceRemote) {
        return languageApi.getLanguageList();
    }

    @Override
    public void addLanguage(Language language) {
        //Currently, we do not need this for remote source.
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void clearData() {
        //Currently, we do not need this for remote source.
        throw new UnsupportedOperationException("Unsupported operation");
    }
}
