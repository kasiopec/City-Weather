package com.kasiopec.cityweather

import android.app.Application
import android.content.Context
import com.kasiopec.cityweather.database.WeatherDB

/**
 * Application class to get the app context for the database, creation of the database.
 * **/
class App : Application(){
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        WeatherDB.create(context)
    }
}