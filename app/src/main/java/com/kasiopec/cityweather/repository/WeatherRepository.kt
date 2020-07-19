package com.kasiopec.cityweather.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.model.OpenWeatherEndpointAPI
import com.kasiopec.cityweather.model.ServiceBuilder
import com.kasiopec.cityweather.model.WeatherData
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository(val database: WeatherDB) {

    private val request: OpenWeatherEndpointAPI = ServiceBuilder
        .buildService(OpenWeatherEndpointAPI::class.java)
    private val apiKey = ***REMOVED***
    private val defaultUnits = "metric"

    val cityWeatherList: LiveData<List<DatabaseEntities.CityWeather>> =
        database.getCityWeatherDao().getAllCities()
    var retrofitErrorMessage = MutableLiveData<String>()
    var isRetrofitError = MutableLiveData<Boolean>(false)

    suspend fun fetchCityWeather(city: String) {
        withContext(Dispatchers.IO) {
            val response = request.getWeatherData(city, apiKey, defaultUnits)
            //Handling non-successful response, network related errors are caught in viewmodel
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    retrofitErrorMessage.value = response.message()
                    isRetrofitError.value = true
                }
                return@withContext
            }
            val results = requireNotNull(response.body())
            setupCityWeatherObject(results)
        }
    }

    suspend fun fetchMultipleCitiesWeather(cities: List<DatabaseEntities.CityWeather>) {
        val cityIdsList = mutableListOf<Int>()
        for (city in cities) {
            cityIdsList.add(city.cityId)
        }
        val cityIds = cityIdsList.joinToString(",")
        withContext(Dispatchers.IO) {
            val response = request.getBulkWeatherData(cityIds, apiKey, defaultUnits)
            //Handling non-successful response, network related errors are caught in viewmodel
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    retrofitErrorMessage.value = response.message()
                    isRetrofitError.value = true
                }
            }
            val results = requireNotNull(response.body())
            for (city in results.list) {
                setupCityWeatherObject(city)
            }
        }
    }

    suspend fun deleteCityWeather(city: DatabaseEntities.CityWeather) {
        withContext(Dispatchers.IO) {
            WeatherDB.create(App.context).getCityWeatherDao().deleteCity(city)
        }
    }

    private fun setupCityWeatherObject(city: WeatherData) {
        val updatedCityList = mutableListOf<DatabaseEntities.CityWeather>()
        val date = Date(city.dt.toLong() * 1000).formatTo("HH:mm, MMM dd")
        val status = city.weather[0].main
        val statusDescription = city.weather[0].description
        val iconUrl = "https://openweathermap.org/img/wn/" + city.weather[0].icon + "@2x.png"
        val formattedTemperature = "%.1f".format(city.main.temp).toDouble()
        val formattedFeelsLike = "%.1f".format(city.main.feelsLike).toDouble()

        val cityItem = DatabaseEntities.CityWeather(
            city.name,
            city.id,
            iconUrl,
            formattedTemperature,
            date,
            status,
            statusDescription,
            formattedFeelsLike,
            city.main.humidity,
            city.wind.speed
        )
        updatedCityList.add(cityItem)

        if (updatedCityList.size == 1) {
            WeatherDB.create(App.context).getCityWeatherDao().insertCity(updatedCityList[0])
        } else {
            WeatherDB.create(App.context).getCityWeatherDao().insertAllCities(updatedCityList)
        }
    }

    private fun Date.formatTo(
        dateFormat: String,
        timeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
}