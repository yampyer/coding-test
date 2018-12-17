package com.jeanpigomez.codingtest.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.jeanpigomez.codingtest.model.LanguageDao;
import com.jeanpigomez.codingtest.model.database.AppDatabase;
import com.jeanpigomez.codingtest.utils.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private static final String DATABASE = "database_name";

    @Provides
    @Named(DATABASE)
    String provideDatabaseName() {
        return Constants.DATABASE_NAME;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabaseDao(Context context, @Named(DATABASE) String databaseName) {
        return Room.databaseBuilder(context, AppDatabase.class, databaseName).build();
    }

    @Provides
    @Singleton
    LanguageDao provideLanguageDao(AppDatabase appDatabase) {
        return appDatabase.languageDao();
    }
}
