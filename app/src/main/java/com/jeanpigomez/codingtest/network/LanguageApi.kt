package com.jeanpigomez.codingtest.network

import com.jeanpigomez.codingtest.model.Language
import io.reactivex.Observable
import retrofit2.http.GET

interface LanguageApi {

    @GET("/items")
    fun getLanguages(): Observable<List<Language>>
}
