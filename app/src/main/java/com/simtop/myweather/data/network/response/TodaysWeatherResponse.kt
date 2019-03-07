package com.simtop.myweather.data.network.response

import com.google.gson.annotations.SerializedName
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.data.db.entity.TodaysWeatherEntry

data class TodaysWeatherResponse(
    val location: WeatherLocation,
    @SerializedName("current")
    val todaysWeatherEntry: TodaysWeatherEntry
)