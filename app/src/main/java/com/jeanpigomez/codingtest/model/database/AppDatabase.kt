package com.jeanpigomez.codingtest.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jeanpigomez.codingtest.model.Language
import com.jeanpigomez.codingtest.model.LanguageDao

@Database(entities = [Language::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}
