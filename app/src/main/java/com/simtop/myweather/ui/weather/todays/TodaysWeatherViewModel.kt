package com.simtop.myweather.ui.weather.todays

import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.internal.lazyDeferred
import com.simtop.myweather.ui.base.WeatherViewModel

class TodaysWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(weatherRepository, unitProvider) {

    val weather by lazyDeferred {
        //we could also call isMetricUnit
        weatherRepository.getTodaysWeather(super.isMetricUnit)
    }
}


