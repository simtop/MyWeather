package com.simtop.myweather.data.network

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.network.response.FutureWeatherResponse
import com.simtop.myweather.data.network.response.TodaysWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedTodaysWeather: LiveData<TodaysWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchTodaysWeather(
        location: String,
        languageCode: String
    )

    suspend fun fetchFutureWeather(
        location: String,
        languageCode: String
    )
}