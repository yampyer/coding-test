package com.jeanpigomez.codingtest.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.jeanpigomez.codingtest.BR;
import com.jeanpigomez.codingtest.utils.Constants;

@Entity(tableName = Constants.LANGUAGE_TABLE_NAME)
public class Language extends BaseObservable {

    @PrimaryKey
    private @NonNull
    String name;
    private int score;
    private String endDate;

    public Language(@NonNull String name, int score, String endDate) {
        this.name = name;
        this.score = score;
        this.endDate = endDate;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        notifyPropertyChanged(BR.score);
    }

    @Bindable
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        notifyPropertyChanged(BR.endDate);
    }
}
