package com.jeanpigomez.codingtest.ui.language;

import dagger.Module;
import dagger.Provides;

@Module
public class LanguagePresenterModule {
    private LanguageContract.View view;

    public LanguagePresenterModule(LanguageContract.View view) {
        this.view = view;
    }

    @Provides
    public LanguageContract.View provideView() {
        return view;
    }
}
