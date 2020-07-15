package com.kasiopec.cityweather.database

import androidx.room.*

@Dao
interface CityWeatherDao {
    @Query("SELECT * FROM weather")
    suspend fun getAllCities() : List<DatabaseEntities.CityWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(cityWeather : DatabaseEntities.CityWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCities(cityList : List<DatabaseEntities.CityWeather>)

    @Delete
    suspend fun deleteCity(cityWeather: DatabaseEntities.CityWeather)
}