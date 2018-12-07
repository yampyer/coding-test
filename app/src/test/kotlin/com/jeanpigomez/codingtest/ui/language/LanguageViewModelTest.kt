package com.jeanpigomez.codingtest.ui.language

import com.jeanpigomez.codingtest.model.Language
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.rules.TestRule

class LanguageViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var viewModel: LanguageViewModel

    @Before
    fun setUp() {
        viewModel = LanguageViewModel()
    }

    @Test
    fun testBind() {
        val language = Language("Java", 12, "2019-02-27T20:30:00.000+0000")
        viewModel.bind(language)
        assertEquals(viewModel.getLanguageName().value, language.name)
        assertEquals(viewModel.getLanguageScore().value, language.score.toString())
    }

    @Test
    fun testCalculateDateDiff() {
        val language = Language("Java", 12, "2019-02-27T20:30:00.000+0000")
        viewModel.bind(language)
        assertEquals(viewModel.getLanguageRemainingTime().value, viewModel.calculateDateDiff("2019-02-27T20:30:00.000+0000"))
    }

    @Test
    fun testCalculateDateDiffLessThanADay() {
        val language = Language("Java", 12, "2018-12-8T05:30:00.000+0000")
        viewModel.bind(language)
        assertEquals(viewModel.getLanguageRemainingTime().value, viewModel.calculateDateDiff("2018-12-8T05:30:00.000+0000"))
    }

    @Test
    fun testCalculateDateDiffPassedScenario() {
        val language = Language("Java", 12, "2018-12-6T05:30:00.000+0000")
        viewModel.bind(language)
        assertEquals(viewModel.getLanguageRemainingTime().value, "Passed!")
    }

}
