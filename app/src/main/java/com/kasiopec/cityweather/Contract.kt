package com.kasiopec.cityweather

import com.kasiopec.cityweather.model.WeatherItem

interface Contract {

    interface MainActivityView {
        fun addCity(city: String)
    }

    interface MainFragmentView {
        fun renderCityList()
    }
    interface DetailsFragmentView{

    }

    interface MainViewPresenter{

    }

    interface MainFragmentPresenter{
        fun getWeatherData() : List<WeatherItem>
    }

    interface DetailsFragmentPresenter{

    }
}