package com.kasiopec.cityweather.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kasiopec.cityweather.App
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    private val myApplication : App  = app as App
    private val weatherRepository  = WeatherRepository(WeatherDB.create(myApplication))

    private var networkErrorMessage = weatherRepository.retrofitErrorMessage
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    private var _isNetworkError = weatherRepository.isRetrofitError

    val networkError: LiveData<String>
        get() = networkErrorMessage

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val isNetworkError : LiveData<Boolean>
        get() = _isNetworkError

    fun loadRepositoryWeatherData(city : String){
        viewModelScope.launch {
            try{
                _isNetworkError.value = false
                _isNetworkErrorShown.value = false
                weatherRepository.fetchCityWeather(city)
            }catch (networkError : UnknownHostException){
                    networkErrorMessage.value = "Network error, check your connection!"
                    _isNetworkError.value = true
            }catch (ex : Exception){
                networkErrorMessage.value = "Unknown error: $ex"
                _isNetworkError.value = true
            }
        }
    }

    fun networkErrorShown() {
        _isNetworkErrorShown.value = true
    }


}