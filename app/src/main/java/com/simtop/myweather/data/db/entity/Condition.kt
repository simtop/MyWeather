package com.simtop.myweather.data.db.entity

data class Condition(
    //the database will see as condition_text, condition_icon, condition_code
    val text: String,
    val icon: String,
    val code: Int
)