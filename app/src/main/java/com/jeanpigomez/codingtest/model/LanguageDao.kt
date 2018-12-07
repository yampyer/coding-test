package com.jeanpigomez.codingtest.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface LanguageDao {
    @get:Query("SELECT * FROM language")
    val all: List<Language>

    @Insert
    fun insertAll(vararg languages: Language)
}
