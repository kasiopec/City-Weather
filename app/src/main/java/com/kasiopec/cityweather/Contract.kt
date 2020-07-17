package com.kasiopec.cityweather

import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.model.CityItem

interface Contract {

    interface MainActivityView {
    }

    interface MainFragmentView {
        fun renderCityList()
    }
    interface DetailsFragmentView{

    }

    interface MainActivityPresenter{
        fun loadCityData(city : String)
    }

    interface MainFragmentPresenter{
        fun loadCityData(city : String)
        fun getWeatherData() : List<CityItem>
        fun getData() : List<DatabaseEntities.CityWeather>
    }

    interface DetailsFragmentPresenter{

    }
}