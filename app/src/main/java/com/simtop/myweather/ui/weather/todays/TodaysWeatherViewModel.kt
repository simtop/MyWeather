package com.simtop.myweather.ui.weather.todays

import androidx.lifecycle.ViewModel;
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.internal.SystemType
import com.simtop.myweather.internal.lazyDeferred

class TodaysWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val systemType = unitProvider.getUnitType()
    val isMetric : Boolean
        get() = systemType == SystemType.METRIC

    val weather by lazyDeferred{
        weatherRepository.getTodaysWeather(isMetric)
    }
}
