package com.simtop.myweather.ui.weather.NextDays.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.simtop.myweather.R

class NextDaysWeatherFragment : Fragment() {

    companion object {
        fun newInstance() = NextDaysWeatherFragment()
    }

    private lateinit var viewModel: NextDaysWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_days_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NextDaysWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
