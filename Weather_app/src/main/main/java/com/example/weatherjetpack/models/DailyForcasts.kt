package com.example.weatherjetpack.models

import com.google.gson.annotations.SerializedName
//represent the structure of the expected JSON response for daily forecasts
//If the API response is expected to be in JSON format and follow the structure
// you've defined, then the type T in your baseModel.Success<T> would be DailyForcasts
data class DailyForcasts(
    @SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecast>
)