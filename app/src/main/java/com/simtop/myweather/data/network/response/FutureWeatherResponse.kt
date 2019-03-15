package com.simtop.myweather.data.network.response

import com.google.gson.annotations.SerializedName
import com.simtop.myweather.data.db.entity.WeatherLocation


data class FutureWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("forecast")
    val futureWeatherEntries: ForecastDaysList
)