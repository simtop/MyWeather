package com.simtop.myweather.ui.weather.nextdays.list

import com.simtop.myweather.R
import com.simtop.myweather.data.db.unittype.future.MetricSimpleFutureWeatherEntry
import com.simtop.myweather.data.db.unittype.future.UnitSpecificSimpleFutureWeatherEntry
import com.simtop.myweather.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
//Look at Gradle App experimental comment
import kotlinx.android.synthetic.main.item_next_days_weather.*

import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class NextDaysWeatherItem(
    val weatherEntry: UnitSpecificSimpleFutureWeatherEntry
) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.conditionText
            updateDate()
            updateTemperature()
            updateConditionImage()
        }
    }

    override fun getLayout() = R.layout.item_next_days_weather

    private fun ViewHolder.updateDate() {
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        textView_date.text = weatherEntry.date.format(dtFormatter)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "°C"
        else "°F"
        textView_temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateConditionImage() {
        GlideApp.with(this.containerView)
            .load("http:" + weatherEntry.conditionIconUrl)
            .into(imageView_condition_icon)
    }
}