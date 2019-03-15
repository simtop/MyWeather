package com.simtop.myweather.data.network.response

import com.google.gson.annotations.SerializedName
import com.simtop.myweather.data.db.entity.FutureWeatherEntry

data class ForecastDaysList(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)