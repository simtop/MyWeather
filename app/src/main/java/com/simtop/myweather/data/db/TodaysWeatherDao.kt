package com.simtop.myweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simtop.myweather.data.db.entity.TODAYS_WEATHER_ID
import com.simtop.myweather.data.db.entity.TodaysWeatherEntry
import com.simtop.myweather.data.db.unittype.today.ImperialTypeTodaysWeather
import com.simtop.myweather.data.db.unittype.today.MetricTypeTodaysWeather

@Dao
interface TodaysWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateAndInsert(todaysWeatherEntry: TodaysWeatherEntry)

    @Query("select * from todays_weather where id = $TODAYS_WEATHER_ID")
    fun getMetricWeather() : LiveData<MetricTypeTodaysWeather>

    @Query("select * from todays_weather where id = $TODAYS_WEATHER_ID")
    fun getImperialWeather() : LiveData<ImperialTypeTodaysWeather>
}