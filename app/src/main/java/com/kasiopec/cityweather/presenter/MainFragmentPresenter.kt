package com.kasiopec.cityweather.presenter

import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.model.CityItem

class MainFragmentPresenter(val view: Contract.MainFragmentView):
    Contract.MainFragmentPresenter {

    override fun getWeatherData(): List<CityItem> {
        TODO("Not yet implemented")
    }

}