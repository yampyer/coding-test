package com.jeanpigomez.codingtest.network;

import com.jeanpigomez.codingtest.model.Language;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface LanguageApi {

    @GET("/items")
    Flowable<List<Language>> getLanguageList();
}
