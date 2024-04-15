package com.example.weatherjetpack.models

import android.health.connect.datatypes.units.Temperature
import com.google.gson.annotations.SerializedName

data class HourlyForecast(
    @SerializedName("Date")
    val date:String,
    @SerializedName("EpochDateTime")
    val epochDateTime:Long,
    @SerializedName("WeatherIcon")
    val weatherIcon:Int,
    @SerializedName("IconPhrase")
    val iconPhrase:String,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation:Boolean,
    @SerializedName("IsDaylight")
    val isDaylight:Boolean,
    @SerializedName("Temperature")
    val temperature: Value
)