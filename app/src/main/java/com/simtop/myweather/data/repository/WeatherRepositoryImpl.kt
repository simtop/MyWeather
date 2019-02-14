package com.simtop.myweather.data.repository

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.db.TodaysWeatherDao
import com.simtop.myweather.data.db.unittype.UnitSpecificType
import com.simtop.myweather.data.network.WeatherNetworkDataSource
import com.simtop.myweather.data.network.response.TodaysWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*

//We can use observeForever and GlobalScope, because the Repository doesn't have a lifecycle, it's destroyed with the app
class WeatherRepositoryImpl(
    private val todaysWeatherDao: TodaysWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : WeatherRepository {
    init {
        weatherNetworkDataSource.downloadedTodaysWeather.observeForever { newTodaysWeather ->
            persistFetchedTodaysWeather(newTodaysWeather)
        }

    }
    override suspend fun getTodaysWeather(type: Boolean): LiveData<out UnitSpecificType> {
        //withContext returns something
        return withContext(Dispatchers.IO){
            initWeatherData()
            return@withContext  if(type) todaysWeatherDao.getMetricWeather()
                                else todaysWeatherDao.getImperialWeather()
        }
    }
    private fun persistFetchedTodaysWeather(fetchedWeather : TodaysWeatherResponse){
        //lanch does a job, doesn't return things
        GlobalScope.launch(Dispatchers.IO) {
            todaysWeatherDao.updateAndInsert(fetchedWeather.todaysWeatherEntry)
        }
    }
    private suspend fun initWeatherData(){
        val dummyTime = ZonedDateTime.now().minusHours(1)
        if(isFetchingTodayNeeded(dummyTime))
            fetchTodaysWeather()
    }

    private  suspend fun fetchTodaysWeather(){
        weatherNetworkDataSource.fetchTodaysWeather(
            "Barcelona",
            Locale.getDefault().language
        )
    }
    private fun isFetchingTodayNeeded(lastFetchTime : ZonedDateTime) : Boolean{
        val halfHourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(halfHourAgo)
    }
}