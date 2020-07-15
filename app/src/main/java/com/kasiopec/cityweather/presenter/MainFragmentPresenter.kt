package com.kasiopec.cityweather.presenter

import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.model.WeatherItem

class MainFragmentPresenter(val view: Contract.MainFragmentView):
    Contract.MainFragmentPresenter {

    override fun getWeatherData(): List<WeatherItem> {
        TODO("Not yet implemented")
    }

}