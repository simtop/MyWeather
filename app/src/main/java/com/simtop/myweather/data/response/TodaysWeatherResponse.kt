package com.simtop.myweather.data.response

import com.google.gson.annotations.SerializedName

data class TodaysWeatherResponse(
    val location: Location,
    @SerializedName("current")
    val todaysWeatherEntry: TodaysWeatherEntry
)