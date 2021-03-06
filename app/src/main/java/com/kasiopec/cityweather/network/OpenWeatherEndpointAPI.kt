package com.kasiopec.cityweather.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface for the retrofit calls
 * **/
interface OpenWeatherEndpointAPI {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(
        @Query("q") cityName: String,
        @Query("APPID") key: String,
        @Query("units") units: String
    ): Response<WeatherData>

    @GET("/data/2.5/group")
    suspend fun getBulkWeatherData(
        @Query("id") cities: String,
        @Query("APPID") key: String,
        @Query("units") units: String
    ): Response<WeatherBulkData>
}