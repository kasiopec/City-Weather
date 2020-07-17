package com.kasiopec.cityweather.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    private val myApplication : App  = app as App
    private val weatherRepository  = WeatherRepository(WeatherDB.create(myApplication))


     fun loadRepositoryWeatherData(city : String){
        viewModelScope.launch {
            try{
                weatherRepository.updateCityWeather(city)
            }catch (networkError : IOException){

            }
        }
    }
}