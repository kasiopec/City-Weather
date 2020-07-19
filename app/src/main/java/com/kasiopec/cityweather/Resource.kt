package com.kasiopec.cityweather

data class Resource<T>(val data: T? = null, val message: String? = null) {
    companion object{
        fun <T> success(data: T?) : Resource<T>{
            return Resource(data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T>{
            return Resource(data, msg)
        }

        fun <T> failure(data: T?): Resource<T>{
            return Resource(data, null)
        }
    }
}