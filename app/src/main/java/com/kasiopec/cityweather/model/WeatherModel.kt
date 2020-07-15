package com.kasiopec.cityweather.model

import retrofit2.Call


class WeatherModel {
    private val request : OpenWeatherEndpointAPI = ServiceBuilder
        .buildService(OpenWeatherEndpointAPI::class.java)
    private lateinit var call : Call<WeatherItem>
    private val API_KEY = ***REMOVED***

    fun fetchWeatherData(city : String, units : String ){
        call = request.getWeatherData(city, API_KEY, units)
    }
}