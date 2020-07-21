package com.kasiopec.cityweather

import androidx.lifecycle.LiveData
import com.kasiopec.cityweather.model.MainActivityViewModel
import com.kasiopec.cityweather.repository.WeatherRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config



/**
 * Test not complete.
 * With this test I wanted to check if values inside viewmodel are updated when retrofit returns
 * non-successful response, however I didn't find the way how to replicate a bad response.
 *
 * In addition to that due to the fact that function inside viewmodel which calls function on repository is
 * launched with the coroutine I had to move entire test logic under one coroutine dispatcher - testDispatcher by making
 * it Main. This way all the coroutine stuff is happening under one "thread"
 * **/
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MainActivityViewModelTest {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var isNetworkErrorShownLiveData: LiveData<Boolean>
    private lateinit var isNetworkErrorLiveData: LiveData<Boolean>

    @ExperimentalCoroutinesApi
    val testDispatcher = TestCoroutineDispatcher()

    @RelaxedMockK
    private lateinit var repo: WeatherRepository

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        val mApplication = App()
        viewModel = MainActivityViewModel(mApplication)
        isNetworkErrorShownLiveData = viewModel.isNetworkErrorShown
        isNetworkErrorLiveData = viewModel.isNetworkError
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `is true when repo throws bad response`() = testDispatcher.runBlockingTest {
        var isNetworkError = isNetworkErrorLiveData.value
        println(isNetworkError)
        assertNotNull(isNetworkError)
        isNetworkError?.let { assertFalse(it) }
        //whenever(repo.fetchCityWeather(anyString())).thenReturn()
        doReturn(Exception()).`when`(repo).fetchCityWeather("Riga")
        viewModel.loadRepositoryWeatherData("Riga")
        verify( repo.fetchCityWeather("Riga") )
        isNetworkError = isNetworkErrorLiveData.value
        println(isNetworkError)
        assertNotNull(isNetworkError)
        isNetworkError?.let { assertTrue(it) }
        return@runBlockingTest
    }
}
