package com.simtop.myweather.data.repository

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.data.db.unittype.UnitSpecificType

interface WeatherRepository {
    /**
     * type true = Metric Type
     * type false = Imperial Type
     */
    suspend fun getTodaysWeather(type : Boolean) : LiveData<out UnitSpecificType>
    suspend fun getWeatherLocation() : LiveData<WeatherLocation>
}