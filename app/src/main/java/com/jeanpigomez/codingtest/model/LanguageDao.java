package com.jeanpigomez.codingtest.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.jeanpigomez.codingtest.utils.Constants;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LanguageDao {
    @Query("SELECT * FROM " + Constants.LANGUAGE_TABLE_NAME)
    Flowable<List<Language>> getAllLanguages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Language language);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Language> languages);

    @Query("DELETE FROM " + Constants.LANGUAGE_TABLE_NAME)
    void deleteAll();
}
