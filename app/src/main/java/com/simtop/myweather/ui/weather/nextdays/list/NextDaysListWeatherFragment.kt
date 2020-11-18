package com.simtop.myweather.ui.weather.nextdays.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.simtop.myweather.R
import com.simtop.myweather.data.db.LocalDateConverter
import com.simtop.myweather.data.db.unittype.future.list.UnitSpecificSimpleFutureWeatherEntry
import com.simtop.myweather.ui.base.ScopedFragment
import com.simtop.myweather.ui.weather.todays.TodaysWeatherViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

import kotlinx.android.synthetic.main.next_days_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate

//ScopedFragment because I want to use coroutines
class NextDaysListWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory : NextDaysListWeatherViewModelFactory by instance<NextDaysListWeatherViewModelFactory>()

    private lateinit var viewModel: NextDaysListWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_days_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NextDaysListWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch(Dispatchers.Main){
        val futureWeatherEntries = viewModel.weatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@NextDaysListWeatherFragment, Observer { location ->
            if (location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeatherEntries.observe(this@NextDaysListWeatherFragment, Observer { weatherEntries ->
            if (weatherEntries == null) return@Observer

            group_loading.visibility = View.GONE

            updateDateToNextWeek()
            initRecyclerView(weatherEntries.toNextDaysWeatherItems())
        })
    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

    private fun initRecyclerView(items: List<NextDaysWeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@NextDaysListWeatherFragment.context)
            adapter = groupAdapter
        }

        groupAdapter.setOnItemClickListener { item, view ->
            (item as? NextDaysWeatherItem)?.let {
                showWeatherDetail(it.weatherEntry.date, view)
            }        }
    }

    private fun showWeatherDetail(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!
        //ReBuilding is needed to autogenerate NextDaysListWeatherFragmentDirections
        val actionDetail = NextDaysListWeatherFragmentDirections.actionDetail(dateString)
        Navigation.findNavController(view).navigate(actionDetail)
    }


    private fun List<UnitSpecificSimpleFutureWeatherEntry>.toNextDaysWeatherItems() : List<NextDaysWeatherItem> {
        return this.map {
            NextDaysWeatherItem(it)
        }
    }

}
