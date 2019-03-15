package com.simtop.myweather.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//indices part is to speed up query indexing date, because we are going to look for dates,
//and unique = true is because we can't have same different information for same day
@Entity(tableName = "future_weather", indices = [Index(value = ["date"], unique = true)])
data class FutureWeatherEntry(
    //autogenerate true because there are more than a single FutureWeatherEntry at one single point of time
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val date: String,
    @Embedded
    val day: Day
)