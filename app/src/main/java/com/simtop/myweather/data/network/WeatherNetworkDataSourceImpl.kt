package com.simtop.myweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.simtop.myweather.data.network.response.FutureWeatherResponse
import com.simtop.myweather.data.network.response.TodaysWeatherResponse
import com.simtop.myweather.internal.NoConnectivityExeption

const val FORECAST_DAYS_COUNT = 7

class WeatherNetworkDataSourceImpl(
    private val apixuService: ApixuService
) : WeatherNetworkDataSource {

    private val _downloadedTodaysWeather = MutableLiveData<TodaysWeatherResponse>()
    override val downloadedTodaysWeather: LiveData<TodaysWeatherResponse>
        get() = _downloadedTodaysWeather

    override suspend fun fetchTodaysWeather(location: String, languageCode: String) {
        try {
            val fetchedTodaysWeather = apixuService
                .getTodaysWeather(location, languageCode)
                .await()
            _downloadedTodaysWeather.postValue(fetchedTodaysWeather)
        }
        catch (e: NoConnectivityExeption) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    ) {
        try {
            val fetchedFutureWeather = apixuService
                .getFutureWeather(location, FORECAST_DAYS_COUNT, languageCode)
                .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        }
        catch (e: NoConnectivityExeption) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}


