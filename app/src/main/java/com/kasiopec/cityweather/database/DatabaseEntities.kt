package com.kasiopec.cityweather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


class DatabaseEntities {
    @Entity(tableName = "weather")
    data class CityWeather constructor(
        @ColumnInfo(name = "city_name")
        var cityName: String = "",
        @PrimaryKey
        @ColumnInfo(name = "city_id")
        var cityId: Int = -1,
        @ColumnInfo(name = "weather_icon_url")
        var weatherIconUrl: String = "",
        @ColumnInfo(name = "current_temp")
        var temp: Double = -1.0,
        @ColumnInfo(name = "date")
        var date: String = "",
        @ColumnInfo(name = "status")
        var status: String = "",
        @ColumnInfo(name = "status_description")
        var statusDescription: String = "",
        @ColumnInfo(name = "feels_like")
        var feelsLike: Double = -1.0,
        @ColumnInfo(name = "humidity")
        var humidity: Int = -1,
        @ColumnInfo(name = "wind_speed")
        var windSpeed: Double = -1.0,
        @ColumnInfo(name = "request_time")
        var requestTime: String = ""
    ) : Serializable
}