package com.kasiopec.cityweather.repository


import androidx.lifecycle.LiveData
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.model.CityItem
import com.kasiopec.cityweather.model.OpenWeatherEndpointAPI
import com.kasiopec.cityweather.model.ServiceBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository(private val database : WeatherDB) {

    private val request: OpenWeatherEndpointAPI = ServiceBuilder
        .buildService(OpenWeatherEndpointAPI::class.java)
    private val apiKey = ***REMOVED***
    private val defaultUnits = "metric"
    val cityWeatherList : LiveData<List<DatabaseEntities.CityWeather>> = database.getCityWeatherDao().getAllCities()

    suspend fun updateCityWeather(city : String){
        withContext(Dispatchers.IO){
            val response = request.getWeatherData(city, apiKey, defaultUnits)
            if(!response.isSuccessful){
                println("error appeared")
            }
            val result = requireNotNull(response.body())
            //DO STUFF WITH THE DATA
            println("Through viewModel")
            println("OPEN WEATHER Response: $result")
            println("City name: " + result.name)

            val date = Date(result.dt.toLong() * 1000).formatTo("HH:mm, MMM dd")
            val status = result.weather[0].main
            val statusDescription = result.weather[0].description
            val formattedTemperature = "%.1f".format(result.main.temp).toDouble()
            val formattedFeelsLike = "%.1f".format(result.main.feelsLike).toDouble()

            val cityItem = DatabaseEntities.CityWeather(
                result.name,
                result.id,
                formattedTemperature,
                date,
                status,
                statusDescription,
                formattedFeelsLike,
                result.main.humidity,
                result.wind.speed
            )
            WeatherDB.create(App.context).getCityWeatherDao().insertCity(cityItem)
            println("Updated as of $date")
        }
    }

    suspend fun deleteCityWeather(city : DatabaseEntities.CityWeather){
        withContext(Dispatchers.IO){
            WeatherDB.create(App.context).getCityWeatherDao().deleteCity(city)
        }
    }



    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
}