package com.kasiopec.cityweather.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kasiopec.cityweather.App


class CustomViewModelFactory(private val mApplication: App) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainActivityViewModel::class.java -> MainActivityViewModel(mApplication)

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}
