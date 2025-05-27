package com.example.core.network.api

import com.example.core.API_KEY
import com.example.core.network.models.HeroResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroApiService {
    @GET("/api/$API_KEY/{id}")
    fun getHeroById(
        @Path("id") id: Long
    ): Call<HeroResponse>

//    @GET("/api/${API_KEY}/search/{name}")
//    fun searchHeroes(
//        @Path("name") name: String
//    ): List<HeroResponse>
}




