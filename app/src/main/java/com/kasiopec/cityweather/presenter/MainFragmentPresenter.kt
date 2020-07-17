package com.kasiopec.cityweather.presenter

import com.kasiopec.cityweather.Contract
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.model.CityItem
import com.kasiopec.cityweather.model.WeatherModel

class MainFragmentPresenter(val view: Contract.MainFragmentView):
    Contract.MainFragmentPresenter {
    private val model = WeatherModel()
    override fun loadCityData(city: String) {
        model.fetchWeatherData(city)
    }

    override fun getWeatherData(): List<CityItem> {
        TODO("Not yet implemented")
    }

    override fun getData(): List<DatabaseEntities.CityWeather> {
        return model.getData()
    }

}