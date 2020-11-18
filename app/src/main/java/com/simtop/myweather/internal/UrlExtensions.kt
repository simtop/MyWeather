package com.simtop.myweather.internal

fun String.formatNewUrl(): String {
    val split = this.split("64x64")
    return "https:${split[0]}128x128${split[1]}"
}