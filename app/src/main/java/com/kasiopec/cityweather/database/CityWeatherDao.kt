package com.kasiopec.cityweather.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityWeatherDao {
    @Query("SELECT * FROM weather")
    fun getAllCities() : LiveData<List<DatabaseEntities.CityWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertCity(cityWeather : DatabaseEntities.CityWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAllCities(cityList : List<DatabaseEntities.CityWeather>)

    @Delete
     fun deleteCity(cityWeather: DatabaseEntities.CityWeather)
}