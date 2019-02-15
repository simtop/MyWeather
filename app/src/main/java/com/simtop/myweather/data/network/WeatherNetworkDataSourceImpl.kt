package com.simtop.myweather.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.simtop.myweather.data.network.response.TodaysWeatherResponse
import com.simtop.myweather.internal.NoConnectivityExeption

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
}


/*
class WeatherNetworkDataSourceImpl(
    private val apixuService: ApixuService
) : WeatherNetworkDataSource {
    private val _downloadedTodaysWeather = MutableLiveData<TodaysWeatherResponse>()
    override val downloadedTodaysWeather: LiveData<TodaysWeatherResponse>
        get() = _downloadedTodaysWeather

    override suspend fun FetchTodaysWeather(location: String, languageCode: String) {
        try {
            val fetchedTodaysWeather = apixuService
                .getTodaysWeather(location,languageCode)
                .await()
            _downloadedTodaysWeather.postValue(fetchedTodaysWeather)
        }
        catch (e : NoConnectivityExeption){
            Log.e("Connectivity", "No Internet Connection", e)
        }
    }
}
        */