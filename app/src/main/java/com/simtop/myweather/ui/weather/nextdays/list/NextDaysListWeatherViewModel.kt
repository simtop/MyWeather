package com.simtop.myweather.ui.weather.nextdays.list

import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.internal.lazyDeferred

import com.simtop.myweather.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class NextDaysListWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(weatherRepository, unitProvider) {

    val weatherEntries by lazyDeferred {
        //we could also call isMetricUnit
        weatherRepository.getFutureWeatherList(LocalDate.now(), super.isMetricUnit)
    }
}
