package com.kasiopec.cityweather.presenter

import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.model.WeatherModel

class MainActivityPresenter(val view: Contract.MainActivityView):
    Contract.MainActivityPresenter {
    private val model = WeatherModel()
    override fun loadCityData(city : String) {
        model.fetchWeatherData(city)
    }


}