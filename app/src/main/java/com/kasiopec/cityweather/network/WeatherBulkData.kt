package com.kasiopec.cityweather.network

/**
 * Retrofit response object for fetching multiple locations
 * **/
class WeatherBulkData(
    val cnt: Int,
    val list: List<WeatherData>
)