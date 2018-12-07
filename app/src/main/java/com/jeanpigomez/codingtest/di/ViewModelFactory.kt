package com.jeanpigomez.codingtest.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import com.jeanpigomez.codingtest.model.database.AppDatabase
import com.jeanpigomez.codingtest.ui.language.LanguageListViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LanguageListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "languages").build()
            @Suppress("UNCHECKED_CAST")
            return LanguageListViewModel(db.languageDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}
