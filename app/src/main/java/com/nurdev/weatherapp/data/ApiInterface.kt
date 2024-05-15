package com.nurdev.weatherapp.data

import com.nurdev.weatherapp.data.models.CurrentWeather
import com.nurdev.weatherapp.utils.Util
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = Util.API_KEY
    ): Response<CurrentWeather>

    @GET("weather?")
    suspend fun getIcon(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = Util.API_KEY
    ): Response<CurrentWeather>
}