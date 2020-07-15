package com.kasiopec.cityweather.model

class CityItem(
    val cityName: String,
    val cityId: Int,
    var temp: Double,
    var date: String,
    var status: String,
    var statusDescription: String,
    var feelsLike: Double,
    var humidity: Int,
    var windSpeed: Double
)
