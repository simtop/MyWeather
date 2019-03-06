package com.simtop.myweather

import android.app.Application
import androidx.preference.PreferenceManager
import com.jakewharton.threetenabp.AndroidThreeTen
import com.simtop.myweather.data.db.WeatherDatabase
import com.simtop.myweather.data.network.*
import com.simtop.myweather.data.provider.UnitProvider
import com.simtop.myweather.data.provider.UnitProviderImpl
import com.simtop.myweather.data.repository.WeatherRepository
import com.simtop.myweather.data.repository.WeatherRepositoryImpl
import com.simtop.myweather.ui.weather.todays.TodaysWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))
        //singleton, because we have only one instance of database
        // we could also do WeatherDatabase.invoke()
        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApixuService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { TodaysWeatherViewModelFactory(instance(), instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //Set dafalut values that are already set on R.xml.preferences, readAgain false because
        //when the user changes preferences it will keep user info in place of our default set values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}