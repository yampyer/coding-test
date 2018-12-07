package com.jeanpigomez.codingtest.base

import android.arch.lifecycle.ViewModel
import com.jeanpigomez.codingtest.di.component.DaggerViewModelInjector
import com.jeanpigomez.codingtest.di.component.ViewModelInjector
import com.jeanpigomez.codingtest.di.module.NetworkModule
import com.jeanpigomez.codingtest.ui.language.LanguageListViewModel
import com.jeanpigomez.codingtest.ui.language.LanguageViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is LanguageListViewModel -> injector.inject(this)
            is LanguageViewModel -> injector.inject(this)
        }
    }
}