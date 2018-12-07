package com.jeanpigomez.codingtest.ui.language

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jeanpigomez.codingtest.model.LanguageDao
import junit.framework.Assert.assertEquals
import utils.mock
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class LanguageListViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: LanguageListViewModel
    var languageDao = mock<LanguageDao>()

    @Before
    fun setUp() {
        viewModel = LanguageListViewModel(languageDao)
    }

    @Test
    fun testLoadLanguages() {
        viewModel.loadLanguages()
    }

    @Test
    fun testOnSortAction() {
        viewModel.onSortAction(1)
        assertEquals(viewModel.sortOption.value, 1)
    }
}