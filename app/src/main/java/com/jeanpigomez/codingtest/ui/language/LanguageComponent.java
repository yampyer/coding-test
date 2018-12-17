package com.jeanpigomez.codingtest.ui.language;

import com.jeanpigomez.codingtest.base.ActivityScope;
import com.jeanpigomez.codingtest.di.LanguageRepositoryComponent;
import com.jeanpigomez.codingtest.utils.schedulers.SchedulerModule;

import dagger.Component;

@ActivityScope
@Component(modules = {LanguagePresenterModule.class, SchedulerModule.class}, dependencies = {
        LanguageRepositoryComponent.class
})
public interface LanguageComponent {
    void inject(LanguageListActivity languageListActivity);
}
