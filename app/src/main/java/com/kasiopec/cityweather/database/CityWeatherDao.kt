package com.kasiopec.cityweather.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CityWeatherDao {

    @Query("SELECT * FROM weather")
    fun getAllCities() : LiveData<List<DatabaseEntities.CityWeather>>

    //Upserts one city to DB (insert or update if data exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsertCity(cityWeather : DatabaseEntities.CityWeather) : Long
    //Upsert all cities to DB (insert or update if data exists)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun upsertAllCities(cityList : List<DatabaseEntities.CityWeather>)

    @Delete
     fun deleteCity(cityWeather: DatabaseEntities.CityWeather)

    //Select all items for ContentProvider
    @Query("SELECT * FROM weather")
    fun getAllCitiesWithCursor() : Cursor

    //Select item for ContentProvider
    @Query("SELECT * FROM weather WHERE city_id = :id")
    fun selectCityById(id: Long): Cursor

    //Delete function for ContentProvider
    @Query("DELETE FROM weather WHERE city_id = :id")
    fun deleteById(id: Long): Int

    //Count function for ContentProvider
    @Query("SELECT COUNT(*) FROM weather")
    fun count(): Int

    @Update
    fun update(cityWeather: DatabaseEntities.CityWeather): Int

    fun updateAll(cityList : List<DatabaseEntities.CityWeather>){
        for(city in cityList){
            update(city)
        }
    }
}