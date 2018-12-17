package com.jeanpigomez.codingtest.di;

import com.jeanpigomez.codingtest.network.repository.LanguageDataSource;
import com.jeanpigomez.codingtest.network.repository.Local;
import com.jeanpigomez.codingtest.network.repository.Remote;
import com.jeanpigomez.codingtest.network.repository.local.LanguageLocalDataSource;
import com.jeanpigomez.codingtest.network.repository.remote.LanguageRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LanguageRepositoryModule {

    @Provides
    @Local
    @Singleton
    public LanguageDataSource provideLocalDataSource(LanguageLocalDataSource languageLocalDataSource) {
        return languageLocalDataSource;
    }

    @Provides
    @Remote
    @Singleton
    public LanguageDataSource provideRemoteDataSource(LanguageRemoteDataSource languageRemoteDataSource) {
        return languageRemoteDataSource;
    }

}
