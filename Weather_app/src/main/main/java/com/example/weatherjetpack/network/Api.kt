package com.example.weatherjetpack.network

import com.example.weatherjetpack.models.DailyForcasts
import com.example.weatherjetpack.models.HourlyForecast
import com.example.weatherjetpack.models.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//this is the API key from MYAPP in weather api
const val APIKEY = "\teHQAYGrVODgg8a9YGskl3WdjxTTTuG8T\n"

//defines an interface named Api for making API requests using Retrofit.
// use the get method to get the endpoint url and map them to get the corresponding data
interface Api {
    //HTTP GET method will be used
    // the relative endpoint URL is "locations/v1/cities/search."
    @GET("locations/v1/cities/search")
    //"suspend" function can be used in the coroutine.
    //function for searching location
    suspend fun searchLocation(
        //specifies a query parameter named "apikey" in the URL
        @Query("apikey") apiKey: String = APIKEY, //apikey
        //specifies a query parameter named "q" in the URL, representing the city name.
        @Query("q") query: String //cityname
        // the API response is expected to be a List of Location
        // objects. The Response class is typically used with Retrofit to wrap the HTTP response.
    ): Response<List<Location>>

    @GET("forecasts/v1/daily/5day/{location_key}")
    suspend fun getDailyForecasts(
        //tells Retrofit that you want to replace the segment in
        // the URL with the value of the locationKey parameter.
        @Path("location_key") locationKey: String,
        @Query("apikey") apiKey: String = APIKEY,
        @Query("metric") metric: Boolean = true
    ): Response<DailyForcasts>

    @GET("forecasts/v1/hourly/12hour/{location_key}")
    suspend fun getHourlyForecasts(
        @Path("location_key")locationKey: String,
        @Query("apikey") apiKey: String = APIKEY,
        @Query("metric") metric: Boolean = true
    ): Response<List<HourlyForecast>>
}