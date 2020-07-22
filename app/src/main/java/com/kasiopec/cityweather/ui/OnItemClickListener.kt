package com.kasiopec.cityweather.ui

import com.kasiopec.cityweather.database.DatabaseEntities

/**
 * Interface fot the recycle view adapter.
 * **/
interface OnItemClickListener {
    fun onItemClicked(item : DatabaseEntities.CityWeather)
    fun onDeleteClicked(item : DatabaseEntities.CityWeather)
}