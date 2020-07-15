package com.kasiopec.cityweather.model

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherEndpointAPI {
    @GET("/data/2.5/weather")
    suspend fun getWeatherData(@Query ("q") cityName : String,
                       @Query("APPID") key : String,
                       @Query("units") units : String
    ) : Response<WeatherData>
}