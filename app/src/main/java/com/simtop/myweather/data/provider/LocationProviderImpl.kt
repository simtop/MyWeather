package com.simtop.myweather.data.provider

import com.simtop.myweather.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        //dummy temporal
        return true
    }

    override suspend fun getPreferedLocationString(): String {
        //dummy temporal
        return "Barcelona"
    }
}