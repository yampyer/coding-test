package com.jeanpigomez.codingtest.network.repository;

import com.jeanpigomez.codingtest.model.Language;

import java.util.List;

import io.reactivex.Flowable;

public interface LanguageDataSource {
    Flowable<List<Language>> loadLanguages(boolean forceRemote);

    void addLanguage(Language language);

    void clearData();
}
