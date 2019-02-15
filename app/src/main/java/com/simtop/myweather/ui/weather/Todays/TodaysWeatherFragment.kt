package com.simtop.myweather.ui.weather.Todays

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.simtop.myweather.R
import com.simtop.myweather.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.todays_weather_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TodaysWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory : TodaysWeatherViewModelFactory by instance()

    private lateinit var viewModel: TodaysWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todays_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TodaysWeatherViewModel::class.java)

        bindUI()


    }
    private fun bindUI() = launch{
        val todaysWeather = viewModel.weather.await()
        todaysWeather.observe(this@TodaysWeatherFragment, Observer {
            if(it == null) return@Observer
            textView.text = it.toString()
        })
    }
}
