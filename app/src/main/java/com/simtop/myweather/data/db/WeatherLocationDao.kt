package com.simtop.myweather.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simtop.myweather.data.db.entity.WEATHER_LOCATION_ID
import com.simtop.myweather.data.db.entity.WeatherLocation

@Dao
interface WeatherLocationDao {

    //Replace, because we have one ID, and we want to replace for new one allways
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateAndInsert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>
}