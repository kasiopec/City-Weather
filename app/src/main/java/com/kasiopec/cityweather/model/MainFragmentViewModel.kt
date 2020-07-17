package com.kasiopec.cityweather.model

import android.app.Application
import androidx.lifecycle.*
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class MainFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val myApplication : App  = app as App
    private val weatherRepository  = WeatherRepository(WeatherDB.create(myApplication))
    val cityWeatherList = weatherRepository.cityWeatherList

    fun deleteWeatherFromRepository(city : DatabaseEntities.CityWeather){
        viewModelScope.launch {
            try{
                weatherRepository.deleteCityWeather(city)
            }catch (networkError : IOException){

            }
        }
    }

}