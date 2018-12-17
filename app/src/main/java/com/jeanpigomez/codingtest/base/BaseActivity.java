package com.jeanpigomez.codingtest.base;

import android.arch.lifecycle.LifecycleRegistry;
import android.support.v7.app.AppCompatActivity;

import com.jeanpigomez.codingtest.AndroidApplication;
import com.jeanpigomez.codingtest.di.LanguageRepositoryComponent;

public class BaseActivity extends AppCompatActivity {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    protected LanguageRepositoryComponent getLanguageRepositoryComponent() {
        return ((AndroidApplication) getApplication()).getLanguageRepositoryComponent();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
