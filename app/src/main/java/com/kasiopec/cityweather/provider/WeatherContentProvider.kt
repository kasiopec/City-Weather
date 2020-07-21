package com.kasiopec.cityweather.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB

class WeatherContentProvider : ContentProvider() {

    //UriMatcher instance to return a value of 1
    // when the URI references the entire products table
    private val CITIES = 1
    //UriMatcher instance to return a value of 2
    //when the URI references the ID of a specific row in the products table
    private val CITY_ID = 2


    private val matcher = UriMatcher(UriMatcher.NO_MATCH)

    init{
        matcher.addURI(AUTHORITY, TABLE, CITIES)
        matcher.addURI(AUTHORITY, "$TABLE/*", CITY_ID)
    }

    companion object{
        private const val AUTHORITY = "com.kasiopec.cityweather.provider"
        private const val TABLE = "weather"
        val CONTENT_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" +
                TABLE)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun delete(uri: Uri, selection: String?,
                        selectionArgs: Array<String>?): Int {
        when(matcher.match(uri)){
            CITIES -> throw IllegalArgumentException("Invalid URI, cannot delete without ID$uri")
            CITY_ID -> {
                val context : Context = context ?: return 0
                val count = WeatherDB.create(context).getCityWeatherDao().deleteById(ContentUris.parseId(uri))
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when(matcher.match(uri)){
            CITIES -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE"
            CITY_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when(matcher.match(uri)){
            CITIES -> {
                val context : Context = context ?: return null
                val id = WeatherDB.create(context).getCityWeatherDao().upsertCity(fromContentValues(values))
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            CITY_ID -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = matcher.match(uri)
        if(code == CITIES || code == CITY_ID){
            val context : Context = context ?: return null
            val cursor : Cursor
            cursor = if(code == CITIES){
                WeatherDB.create(context).getCityWeatherDao().getAllCitiesWithCursor()
            } else{
                WeatherDB.create(context).getCityWeatherDao().selectCityById(ContentUris.parseId(uri))
            }
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }else{
            throw IllegalArgumentException("Unknown URI: $uri")
        }

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
         when(matcher.match(uri)){
            CITIES -> throw IllegalArgumentException("Invalid URI, cannot update without ID$uri")
            CITY_ID -> {
                val context : Context = context ?: return 0
                val cityWeather = fromContentValues(values)
                cityWeather.cityId = ContentUris.parseId(uri).toInt()
                context.contentResolver.notifyChange(uri, null)
                return WeatherDB.create(context).getCityWeatherDao().update(cityWeather)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    /**
     * Create a new [CityWeather] from the specified [ContentValues].
     *
     * @param values A [ContentValues] that at least contain [city_id].
     * @return A newly created [CityWeather] instance.
     */
    private fun fromContentValues(values: ContentValues?): DatabaseEntities.CityWeather {
        var cityWeather = DatabaseEntities.CityWeather()
        if (values != null && values.containsKey("city_id")) {
            cityWeather = DatabaseEntities.CityWeather().apply {
                cityName =  values.getAsString("city_name")
                cityId = values.getAsInteger("city_id")
                humidity = values.getAsInteger("humidity")
                feelsLike =  values.getAsDouble("feels_like")
                windSpeed = values.getAsDouble("wind_speed")
                temp = values.getAsDouble("current_temp")
                requestTime = values.getAsString("request_time")
                status = values.getAsString("status")
                statusDescription = values.getAsString("status_description")
                date = values.getAsString("date")
                weatherIconUrl = values.getAsString("weather_icon_url")
            }
        }
        return cityWeather
    }
}