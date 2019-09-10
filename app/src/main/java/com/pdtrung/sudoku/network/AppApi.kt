package com.pdtrung.sudoku.network

import com.pdtrung.sudoku.model.ResponseCategory
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApi {
    @GET("/api/get-cat/?")
    fun getCategory(
        @Query("apikey") apikey: String,
        @Query("apid") apid: String,
        @Query("type") type: String
    ): Observable<ResponseCategory>
}