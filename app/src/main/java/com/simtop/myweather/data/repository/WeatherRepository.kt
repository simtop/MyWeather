package com.simtop.myweather.data.repository

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.data.db.unittype.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.simtop.myweather.data.db.unittype.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.simtop.myweather.data.db.unittype.today.UnitSpecificType
import org.threeten.bp.LocalDate

interface WeatherRepository {
    /**
     * type true = Metric Type
     * type false = Imperial Type
     */
    suspend fun getTodaysWeather(type : Boolean) : LiveData<out UnitSpecificType>
    suspend fun getFutureWeatherList(startDate: LocalDate, type: Boolean): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>>
    suspend fun getFutureWeatherByDate(date: LocalDate, metric: Boolean): LiveData<out UnitSpecificDetailFutureWeatherEntry>
    suspend fun getWeatherLocation() : LiveData<WeatherLocation>

}