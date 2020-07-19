package com.kasiopec.cityweather.model

import android.app.Application
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


class WeatherModel {
    private val request: OpenWeatherEndpointAPI = ServiceBuilder
        .buildService(OpenWeatherEndpointAPI::class.java)
    private lateinit var call: Call<WeatherData>
    private val apiKey = ***REMOVED***
    private val defaultUnits = "metric"
    private var cityList = mutableListOf<DatabaseEntities.CityWeather>()

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }

    fun fetchWeatherData(city: String) {
        GlobalScope.launch(Dispatchers.IO){
            val response = request.getWeatherData(city, apiKey, defaultUnits)
            if(!response.isSuccessful){
                println("error appeared")
            }
            val result = requireNotNull(response.body())
            //DO STUFF WITH THE DATA
            println("OPEN WEATHER Response: $result")
            println("City name: " + result.name)

            val date = Date(result.dt.toLong() * 1000).formatTo("HH:mm, MMM dd")
            val status = result.weather[0].main
            val statusDescription = result.weather[0].description

            val cityItem = DatabaseEntities.CityWeather(
                result.name,
                result.id,
                "https://openweathermap.org/img/wn/"+result.weather[0].icon+"@2x.png",
                result.main.temp,
                date,
                status,
                statusDescription,
                result.main.feelsLike,
                result.main.humidity,
                result.wind.speed
            )

            WeatherDB.create(App.context).getCityWeatherDao().insertCity(cityItem)
            println("Updated as of $date")

        }
    }

    fun getData(): List<DatabaseEntities.CityWeather> {
        return cityList
    }
}