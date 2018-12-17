package com.jeanpigomez.codingtest.di;

import com.jeanpigomez.codingtest.network.repository.LanguageRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LanguageRepositoryModule.class, AppModule.class, ApiServiceModule.class,
        DatabaseModule.class})
public interface LanguageRepositoryComponent {
    LanguageRepository provideLanguageRepository();
}
