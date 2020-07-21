package com.kasiopec.cityweather.model

import android.app.Application
import androidx.lifecycle.*
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class MainFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val myApplication : App  = app as App
    private val weatherRepository  = WeatherRepository(WeatherDB.create(myApplication))
    val cityWeatherList = weatherRepository.cityWeatherList

    private var _networkErrorMessage = weatherRepository.retrofitErrorMessage
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    private var _isNetworkError = weatherRepository.isRetrofitError

    val networkErrorMessage: LiveData<String>
        get() = _networkErrorMessage

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val isNetworkError : LiveData<Boolean>
        get() = _isNetworkError

    fun deleteWeatherFromRepository(city : DatabaseEntities.CityWeather){
        viewModelScope.launch(Dispatchers.Main) {
                weatherRepository.deleteCityWeather(city)
        }
    }

    fun updateCitiesWeather(cityList : List<DatabaseEntities.CityWeather>){
        viewModelScope.launch {
            try {
                _isNetworkError.value = false
                _isNetworkErrorShown.value = false
                weatherRepository.fetchMultipleCitiesWeather(cityList)
            }catch (networkError : UnknownHostException){
                _networkErrorMessage.value = "Network error, check your connection!"
                _isNetworkError.value = true
            }catch (ex : Exception){
                _networkErrorMessage.value = "Unknown error: $ex"
                _isNetworkError.value = true
            }
        }
    }

    fun networkErrorShown() {
        _isNetworkErrorShown.value = true
    }
}