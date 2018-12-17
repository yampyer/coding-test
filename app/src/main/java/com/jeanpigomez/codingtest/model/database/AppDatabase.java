package com.jeanpigomez.codingtest.model.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.jeanpigomez.codingtest.model.Language;
import com.jeanpigomez.codingtest.model.LanguageDao;

@Database(entities = Language.class, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract LanguageDao languageDao();
}
