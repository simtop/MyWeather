package com.simtop.myweather.data.repository

import androidx.lifecycle.LiveData
import com.simtop.myweather.data.db.FutureWeatherDao
import com.simtop.myweather.data.db.TodaysWeatherDao
import com.simtop.myweather.data.db.WeatherLocationDao
import com.simtop.myweather.data.db.entity.WeatherLocation
import com.simtop.myweather.data.db.unittype.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.simtop.myweather.data.db.unittype.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.simtop.myweather.data.db.unittype.today.UnitSpecificType
import com.simtop.myweather.data.network.FORECAST_DAYS_COUNT
import com.simtop.myweather.data.network.WeatherNetworkDataSource
import com.simtop.myweather.data.network.response.FutureWeatherResponse
import com.simtop.myweather.data.network.response.TodaysWeatherResponse
import com.simtop.myweather.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.*

//We can use observeForever and GlobalScope, because the Repository doesn't have a lifecycle, it's destroyed with the app
class WeatherRepositoryImpl(
    private val todaysWeatherDao: TodaysWeatherDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    init {
        weatherNetworkDataSource.apply {
            downloadedTodaysWeather.observeForever { newTodaysWeather ->
                persistFetchedTodaysWeather(newTodaysWeather)
            }
            downloadedFutureWeather.observeForever { newFutureWeather ->
                persistFetchedFutureWeather(newFutureWeather)
            }
        }

    }

    override suspend fun getTodaysWeather(type: Boolean): LiveData<out UnitSpecificType> {
        //withContext returns something
        return withContext(Dispatchers.IO) {
            initWeatherData()
            //type = true if metric
            return@withContext if (type) todaysWeatherDao.getMetricWeather()
            else todaysWeatherDao.getImperialWeather()
        }
    }

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        type: Boolean
    ): LiveData<out List<UnitSpecificSimpleFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            //type = true if metric
            return@withContext if (type) futureWeatherDao.getSimpleFutureWeatherMetric(startDate)
            else futureWeatherDao.getSimpleFutureWeatherImperial(startDate)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: LocalDate,
        metric: Boolean
    ): LiveData<out UnitSpecificDetailFutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            //init Not needed, because if we are here we alredy used getFutureWeatherList, but just to be safe
            initWeatherData()
            return@withContext if (metric) futureWeatherDao.getDetailedWeatherByDateMetric(date)
            else futureWeatherDao.getDetailedWeatherByDateImperial(date)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedTodaysWeather(fetchedWeather: TodaysWeatherResponse) {
        //lanch does a job, doesn't return things
        GlobalScope.launch(Dispatchers.IO) {
            todaysWeatherDao.updateAndInsert(fetchedWeather.todaysWeatherEntry)
            weatherLocationDao.updateAndInsert(fetchedWeather.location)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {
        fun deleteOldForecastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            val futureWeatherList = fetchedWeather.futureWeatherEntries.entries
            futureWeatherDao.insert(futureWeatherList)
            weatherLocationDao.updateAndInsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()

        if (lastWeatherLocation == null ||
            locationProvider.hasLocationChanged(lastWeatherLocation)
        ) {
            fetchTodaysWeather()
            fetchFutureWeather()
            return
        }

        if (isFetchingTodayNeeded(lastWeatherLocation.zonedDateTime))
            fetchTodaysWeather()

        if(isFetchingFutureNeeded())
            fetchFutureWeather()
    }

    private suspend fun fetchTodaysWeather() {
        weatherNetworkDataSource.fetchTodaysWeather(
            locationProvider.getPreferedLocationString(),
            Locale.getDefault().language
        )
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDataSource.fetchFutureWeather(
            locationProvider.getPreferedLocationString(),
            Locale.getDefault().language
        )
    }

    private fun isFetchingTodayNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val halfHourAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(halfHourAgo)
    }
    private fun isFetchingFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        //forecast_days_count = 7
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }
}