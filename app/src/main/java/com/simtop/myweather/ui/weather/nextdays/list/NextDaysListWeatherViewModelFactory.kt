package com.simtop.myweather.ui.weather.nextdays.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository

class NextDaysListWeatherViewModelFactory(
    private val forecastRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NextDaysListWeatherViewModel(
            forecastRepository,
            unitProvider
        ) as T
    }
}