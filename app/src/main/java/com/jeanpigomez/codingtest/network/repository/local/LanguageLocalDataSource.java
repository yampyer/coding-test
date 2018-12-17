package com.jeanpigomez.codingtest.network.repository.local;

import com.jeanpigomez.codingtest.model.Language;
import com.jeanpigomez.codingtest.model.LanguageDao;
import com.jeanpigomez.codingtest.network.repository.LanguageDataSource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LanguageLocalDataSource implements LanguageDataSource {

    private LanguageDao languageDao;

    @Inject
    public LanguageLocalDataSource(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    @Override
    public Flowable<List<Language>> loadLanguages(boolean forceRemote) {
        return languageDao.getAllLanguages();
    }

    @Override
    public void addLanguage(Language language) {
        // Insert new one
        languageDao.insert(language);
    }

    @Override
    public void clearData() {
        // Clear old data
        languageDao.deleteAll();
    }
}
