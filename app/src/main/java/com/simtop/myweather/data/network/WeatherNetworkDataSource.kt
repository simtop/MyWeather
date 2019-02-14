package com.simtop.myweather.data.network

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.network.response.TodaysWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedTodaysWeather : LiveData<TodaysWeatherResponse>

    suspend fun FetchTodaysWeather(
        location : String ,
        languageCode : String
    )
}