package com.simtop.myweather.ui.weather.todays

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.simtop.myweather.data.repository.WeatherRepository

class TodaysWeatherViewModelFactory(
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodaysWeatherViewModel(weatherRepository) as T
    }
}