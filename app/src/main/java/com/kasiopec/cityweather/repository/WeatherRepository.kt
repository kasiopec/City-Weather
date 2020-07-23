package com.kasiopec.cityweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.network.OpenWeatherEndpointAPI
import com.kasiopec.cityweather.network.ServiceBuilder
import com.kasiopec.cityweather.network.WeatherData
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository(val database: WeatherDB) {

    private val request: OpenWeatherEndpointAPI = ServiceBuilder
        .buildService(OpenWeatherEndpointAPI::class.java)
    //Api key required for the OpenWeather API, api key should not be in the code when goes into production
    private val apiKey = TODO("Enter OpenWeather API key here")
    //Default metric units for the weather data. Check API docs for more options
    private val defaultUnits = "metric"

    val cityWeatherList: LiveData<List<DatabaseEntities.CityWeather>> =
        database.getCityWeatherDao().getAllCities()
    var retrofitErrorMessage = MutableLiveData<String>()
    var isRetrofitError = MutableLiveData<Boolean>(false)

    /**
     * Fetches single city by providing name of city.
     * Function performs Retrofit call, receives response object and calls setup city method
     * to convert the response into  database entity item.
     * Paired with Coroutines to avoid locking main UI thread
     * **/
    suspend fun fetchCityWeather(city: String) {
        withContext(Dispatchers.IO) {
            val response = request.getWeatherData(city, apiKey, defaultUnits)
            val updateTime = Calendar.getInstance().time
            //Handling non-successful response, network related errors are caught in viewmodel
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    retrofitErrorMessage.value = response.message()
                    isRetrofitError.value = true
                }
                return@withContext
            }
            val results = requireNotNull(response.body())
            val list = listOf(results)
            setupCityWeatherObject(list, updateTime)
        }
    }

    /**
     * Fetches multiple cities by providing list of [CityWeather] objects.
     * Function extracts city ids from the objects, joins all of them in a single string, and
     * performs Retrofit call. Paired with Coroutines to avoid locking main UI thread
     * **/
    suspend fun fetchMultipleCitiesWeather(cities: List<DatabaseEntities.CityWeather>) {
        //extracting cityIds into a list, so then joinToString creates string of ints for query
        val cityIdsList = mutableListOf<Int>()
        for (city in cities) {
            cityIdsList.add(city.cityId)
        }
        val cityIdsString = cityIdsList.joinToString(",")

        withContext(Dispatchers.IO) {
            val updateTime = Calendar.getInstance().time
            val response = request.getBulkWeatherData(cityIdsString, apiKey, defaultUnits)
            if (!response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    retrofitErrorMessage.value = response.message()
                    isRetrofitError.value = true
                }
            }
            val results = requireNotNull(response.body())
            setupCityWeatherObject(results.list, updateTime)
        }
    }

    /**
     * Deletes weather object from the Room database, paired with Coroutines to avoid locking
     * main UI thread
     * **/
    suspend fun deleteCityWeather(city: DatabaseEntities.CityWeather) {
        withContext(Dispatchers.IO) {
            WeatherDB.create(App.context).getCityWeatherDao().deleteCity(city)
        }
    }

    /**
     * Function that sets up [CityWeather] object and adds it to the Room database
     * Receives list of [WeatherData] objects, received from the API request call.
     * **/
    private fun setupCityWeatherObject(cityList: List<WeatherData>, date: Date) {
        val updateList = mutableListOf<DatabaseEntities.CityWeather>()
        for (city in cityList) {
            val serverUpdateDate = Date(city.dt.toLong() * 1000).formatTo("HH:mm, MMM dd")
            val status = city.weather[0].main
            val statusDescription = city.weather[0].description
            val iconUrl = "https://openweathermap.org/img/wn/" + city.weather[0].icon + "@2x.png"
            val formattedTemperature = "%.1f".format(city.main.temp).toDouble()
            val formattedFeelsLike = "%.1f".format(city.main.feelsLike).toDouble()
            val requestDate = date.formatTo("EEE dd, HH:mm")

            val cityItem = DatabaseEntities.CityWeather(
                city.name,
                city.id,
                iconUrl,
                formattedTemperature,
                serverUpdateDate,
                status,
                statusDescription,
                formattedFeelsLike,
                city.main.humidity,
                city.wind.speed,
                requestDate
            )
            updateList.add(cityItem)
        }

        if (updateList.size == 1) {
            WeatherDB.create(App.context).getCityWeatherDao().upsertCity(updateList[0])
        } else {
            WeatherDB.create(App.context).getCityWeatherDao().updateAll(updateList)
        }
    }
    /**
     * Extension function to format a date variable
     * **/
    private fun Date.formatTo(
        dateFormat: String,
        timeZone: TimeZone = TimeZone.getDefault()
    ): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
}