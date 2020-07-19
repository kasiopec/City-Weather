package com.kasiopec.cityweather.ui

import com.kasiopec.cityweather.database.DatabaseEntities

interface OnItemClickListener {
    fun onItemClicked(item : DatabaseEntities.CityWeather)
    fun onDeleteClicked(item : DatabaseEntities.CityWeather)
}