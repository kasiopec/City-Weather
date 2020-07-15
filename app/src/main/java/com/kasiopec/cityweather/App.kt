package com.kasiopec.cityweather

import android.app.Application
import android.content.Context
import com.kasiopec.cityweather.database.WeatherDB

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