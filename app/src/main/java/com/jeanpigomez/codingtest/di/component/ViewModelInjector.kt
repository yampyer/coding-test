package com.jeanpigomez.codingtest.di.component

import com.jeanpigomez.codingtest.di.module.NetworkModule
import com.jeanpigomez.codingtest.ui.language.LanguageListViewModel
import com.jeanpigomez.codingtest.ui.language.LanguageViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for viewModels.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(languageListViewModel: LanguageListViewModel)

    fun inject(languageViewModel: LanguageViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
