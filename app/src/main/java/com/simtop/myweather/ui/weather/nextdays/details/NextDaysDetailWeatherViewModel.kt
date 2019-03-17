package com.simtop.myweather.ui.weather.nextdays.details

import androidx.lifecycle.ViewModel;
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.internal.lazyDeferred
import com.simtop.myweather.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class NextDaysDetailWeatherViewModel(
    //look
    private val detailDate: LocalDate,
    private val forecastRepository: WeatherRepository,
    unitProvider: UnitProvider
) : WeatherViewModel(forecastRepository, unitProvider) {

    val weather by lazyDeferred {
        forecastRepository.getFutureWeatherByDate(detailDate, super.isMetricUnit)
    }
}