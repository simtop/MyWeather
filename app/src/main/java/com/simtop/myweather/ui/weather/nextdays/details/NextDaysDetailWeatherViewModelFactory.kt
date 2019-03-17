package com.simtop.myweather.ui.weather.nextdays.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import org.threeten.bp.LocalDate

class NextDaysDetailWeatherViewModelFactory(
    private val detailDate: LocalDate,
    private val forecastRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NextDaysDetailWeatherViewModel(detailDate, forecastRepository, unitProvider) as T
    }
}