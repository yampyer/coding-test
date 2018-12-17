package com.jeanpigomez.codingtest;

import android.app.Application;

import com.jeanpigomez.codingtest.di.AppModule;
import com.jeanpigomez.codingtest.di.LanguageRepositoryComponent;
import com.jeanpigomez.codingtest.di.DaggerLanguageRepositoryComponent;
import com.squareup.leakcanary.LeakCanary;

public class AndroidApplication extends Application {

    private LanguageRepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDependencies();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void initializeDependencies() {
        repositoryComponent = DaggerLanguageRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public LanguageRepositoryComponent getLanguageRepositoryComponent() {
        return repositoryComponent;
    }
}
