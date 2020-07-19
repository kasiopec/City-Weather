package com.kasiopec.cityweather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

class DatabaseEntities {
    @Entity(tableName = "weather")
    data class CityWeather constructor(
        @PrimaryKey
        @ColumnInfo(name = "city_name")
        val cityName: String,
        @ColumnInfo(name = "city_id")
        val cityId: Int,
        @ColumnInfo(name = "weather_icon_url")
        val weatherIconUrl: String,
        @ColumnInfo(name = "current_temp")
        var temp: Double,
        @ColumnInfo(name = "date")
        var date: String,
        @ColumnInfo(name = "status")
        var status: String,
        @ColumnInfo(name = "status_description")
        var statusDescription: String,
        @ColumnInfo(name = "feels_like")
        var feelsLike: Double,
        @ColumnInfo(name = "humidity")
        var humidity: Int,
        @ColumnInfo(name = "wind_speed")
        var windSpeed: Double
    ) : Serializable
}