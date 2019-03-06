package com.simtop.myweather.data.provider

import com.simtop.myweather.internal.SystemType

interface UnitProvider {
    fun getUnitType() : SystemType
}