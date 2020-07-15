package com.kasiopec.cityweather.model

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private var cityList = mutableListOf<CityItem>()

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
        call = request.getWeatherData(city, apiKey, defaultUnits)
        call.enqueue(object : Callback<WeatherData> {
            override fun onResponse(
                call: Call<WeatherData>,
                response: Response<WeatherData>
            ) {
                //catching non failing codes
                if (!response.isSuccessful) {
                    println("Code: " + response.code())
                    return
                }
                val result = requireNotNull(response.body())
                //DO STUFF WITH THE DATA
                println("OPEN WEATHER Response: $result")
                println("City name: " + result.name)
                val date = Date(result.dt.toLong() * 1000).formatTo("HH:mm, MMM dd")
                val status = result.weather[0].main
                val statusDescription = result.weather[0].description
                val city = CityItem(
                    result.name,
                    result.id,
                    result.main.temp,
                    date,
                    status,
                    statusDescription,
                    result.main.feelsLike,
                    result.main.humidity,
                    result.wind.speed
                )

                cityList.add(city)
                println("Updated as of $date")


            }

            //method to catch failed calls
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                println(t.message)
            }
        })
    }
}