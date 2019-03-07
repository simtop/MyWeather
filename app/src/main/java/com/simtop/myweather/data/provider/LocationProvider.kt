package com.simtop.myweather.data.provider

import com.simtop.myweather.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation) : Boolean
    suspend fun getPreferedLocationString() : String
}