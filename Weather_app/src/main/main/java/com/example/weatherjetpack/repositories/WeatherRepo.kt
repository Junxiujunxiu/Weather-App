package com.example.weatherjetpack.repositories

import com.example.weatherjetpack.models.DailyForcasts
import com.example.weatherjetpack.models.HourlyForecast
import com.example.weatherjetpack.models.Location
import com.example.weatherjetpack.models.BaseModel

//weather repository that  is a design pattern used to abstract
// the data access logic in an application.
interface WeatherRepo {
    //responsible for searching locations based on a query string
    // takes a query parameter representing the city name to search for
    //returns a BaseModel wrapping a List of Location objects. The BaseModel is a generic class
    // that typically holds information about the success or failure of the operation and the data related to the operation.
    suspend fun searchLocation(query:String): BaseModel<List<Location>>
    //returns a BaseModel wrapping a single DailyForcasts object.
    suspend fun getDailyForecasts(locationKey:String):BaseModel<DailyForcasts>
    //returns a BaseModel wrapping a List of HourlyForecast objects.
    suspend fun getHourlyForecasts(locationKey: String):BaseModel<List<HourlyForecast>>
}

//they are different functions from Api.kt with different purposes