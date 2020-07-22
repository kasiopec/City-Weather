package com.kasiopec.cityweather.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(version = 1, entities = [DatabaseEntities.CityWeather::class])
abstract class WeatherDB : RoomDatabase(){
    // Logic taken from
    // https://github.com/android/sunflower google open docs
    companion object{
        //Guarding from creating more than one database instance
        @Volatile
        private var instance : WeatherDB? = null

        fun create(context: Context) : WeatherDB{
            return instance ?: synchronized(this){
                instance
                    ?: buildDatabase(context).also { instance = it}
            }
        }

        private fun buildDatabase(context: Context) : WeatherDB {
            return Room.databaseBuilder(context, WeatherDB::class.java, "weather").build()
        }

    }

    abstract fun getCityWeatherDao() : CityWeatherDao
}