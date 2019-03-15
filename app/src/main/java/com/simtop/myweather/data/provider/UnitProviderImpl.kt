package com.simtop.myweather.data.provider

import android.content.Context
import com.simtop.myweather.internal.SystemType

const val UNIT_TYPE ="UNIT_TYPE"

//Class before Interfece because of convention
class UnitProviderImpl(context : Context) : PreferenceProvider(context), UnitProvider {

    override fun getUnitType(): SystemType {
        val selectedName = preferences.getString(UNIT_TYPE,SystemType.METRIC.name)
        //!! = Sure is not Null, but in this case I can skip it, because I have default value SystemType.METRIC.name
        return SystemType.valueOf(selectedName!!)
    }
}