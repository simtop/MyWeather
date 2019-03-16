package com.simtop.myweather.ui.base

import androidx.lifecycle.ViewModel
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.internal.SystemType
import com.simtop.myweather.internal.lazyDeferred

abstract class WeatherViewModel(private val weatherRepository: WeatherRepository,
                       unitProvider: UnitProvider
) : ViewModel() {

    private val systemType = unitProvider.getUnitType()

    val isMetricUnit : Boolean
        get() = systemType == SystemType.METRIC

    val weatherLocation by lazyDeferred{
        weatherRepository.getWeatherLocation()
    }
}