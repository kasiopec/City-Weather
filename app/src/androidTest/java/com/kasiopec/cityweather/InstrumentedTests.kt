package com.kasiopec.cityweather

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kasiopec.cityweather.database.CityWeatherDao
import com.kasiopec.cityweather.database.DatabaseEntities
import com.kasiopec.cityweather.database.WeatherDB
import com.kasiopec.cityweather.provider.WeatherContentProvider
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.*


/**
 * Test that check ContentProvider implementation by emulating ContentReslover actions
 ***/
@RunWith(AndroidJUnit4::class)
class WeatherProviderTest {
    private lateinit var contentResolver: ContentResolver
    private lateinit var db: WeatherDB

    @Before
    fun setUp() {
        val provider = WeatherContentProvider()
        provider.onCreate()
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context.applicationContext, WeatherDB::class.java).build()
        contentResolver = context.contentResolver

    }

    /**
     * Checks if there is no items in the local database.
     * Make sure you removed ALL the items from the database or just wipe the storage data
     * **/
    @Test
    fun no_items_in_db() {
        val cursor = contentResolver.query(WeatherContentProvider.CONTENT_URI,
            arrayOf("city_id"), null, null, null
        )
        assertThat(cursor, notNullValue())
        if (cursor != null) {
            assertThat(cursor.count, `is`(0))
        }
        cursor?.close()
    }

    /**
     * Test checks insert function, query function and delete function
     * Make sure you removed ALL the items from the database or just wipe the storage data
     * **/
    @Test
    fun insert_query_delete(){
        // Insertion of the item
        val itemUri : Uri =
            contentResolver.insert(WeatherContentProvider.CONTENT_URI, cityContentValues("Test"))!!
        assertThat(itemUri, notNullValue())

        // Query of the item
        var cursor = contentResolver.query(WeatherContentProvider.CONTENT_URI,
            arrayOf("city_name"), null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("city_name")), `is`("Test"))

        // Deletion of the item
        val count  = contentResolver.delete(itemUri, null, null)
        assertThat(count, `is`(1))

        cursor = contentResolver.query(WeatherContentProvider.CONTENT_URI,
            arrayOf("city_name"), null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    /**
     * Checks update function, paired with insert function.
     * Make sure you removed ALL the items from the database or just wipe the storage data
     * After completion you can check the item in the app
     * **/
    @Test
    fun insert_and_update(){
        val itemUri : Uri =
            contentResolver.insert(WeatherContentProvider.CONTENT_URI, cityContentValues("Test"))!!
        assertThat(itemUri, notNullValue())
        val count  = contentResolver.update(itemUri, cityContentValues("Updated"), null, null)
        assertThat(count, `is`(1))
        val cursor = contentResolver.query(WeatherContentProvider.CONTENT_URI,
            arrayOf("city_name"), null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("city_name")), `is`("Updated"))
        cursor.close()
    }

    /**
     * Checks delete function, paired with the insert function.
     * Implemented just to clean previous test data
     * **/
    @Test
    fun delete(){
        val itemUri : Uri =
            contentResolver.insert(WeatherContentProvider.CONTENT_URI, cityContentValues("Updated"))!!
        assertThat(itemUri, notNullValue())
        val count  = contentResolver.delete(itemUri, null, null)
        assertThat(count, `is`(1))
    }

    private fun cityContentValues(name : String): ContentValues? {
        val values = ContentValues()
        values.put("city_name", name)
        values.put("city_id", 1111)
        values.put("humidity", 10)
        values.put("weather_icon_url", "https://openweathermap.org/img/wn/03d@2x.png")
        values.put("date", "20.20.20")
        values.put("status", "clouds")
        values.put("status_description", "heavy clouds")
        values.put("request_time", "18:00")
        values.put("current_temp", 20.5)
        values.put("feels_like", 18.0)
        values.put("wind_speed", 2.5)
        return values
    }
}


/**
 * Test that checks Room database and LiveData functionality
 * Adding [CityWeather] object into the database with the upsertCity function
 * Observing Room database getAllCities function return value with the LiveData Observable
 ***/
@RunWith(AndroidJUnit4::class)
class RoomTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val mockitoRule : MockitoRule = MockitoJUnit.rule()

    private lateinit var weatherDao: CityWeatherDao
    private lateinit var db: WeatherDB

    @Mock
    lateinit var observer: Observer<List<DatabaseEntities.CityWeather>>

    @Before
    @Throws(Exception::class)
    fun createDb() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, WeatherDB::class.java).allowMainThreadQueries()
            .build()
        weatherDao = db.getCityWeatherDao()
    }

    @Test
    fun insertAndRead() {
        val cityWeather = DatabaseEntities.CityWeather().apply {
            cityId = 1111
            humidity = 10
            cityName = "TestCity"
            weatherIconUrl = "test url"
            date = "20.20.20"
            status = "clouds"
            statusDescription = "heavy clouds"
            requestTime = "18:00"
            temp = 20.5
            feelsLike = 18.0
            windSpeed = 2.5
        }
        // given
        weatherDao.getAllCities().observeForever(observer)
        // when
        weatherDao.upsertCity(cityWeather)
        // then
        verify(observer).onChanged(Collections.singletonList(cityWeather))
    }
}
