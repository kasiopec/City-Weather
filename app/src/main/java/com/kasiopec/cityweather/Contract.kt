package com.kasiopec.cityweather

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
        fun getWeatherData() : List<CityItem>
    }

    interface DetailsFragmentPresenter{

    }
}