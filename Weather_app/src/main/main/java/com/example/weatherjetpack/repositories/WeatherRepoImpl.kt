package com.example.weatherjetpack.repositories

import com.example.weatherjetpack.models.DailyForcasts
import com.example.weatherjetpack.models.HourlyForecast
import com.example.weatherjetpack.models.Location
import com.example.weatherjetpack.models.BaseModel
import com.example.weatherjetpack.network.Api
import retrofit2.Response

//weather repository implementation
//a class that serves as the implementation of the
// WeatherRepo interface. It is responsible for providing the
// actual logic to search for locations, retrieve daily and
// hourly forecasts, and possibly handle other weather-related
// data operations.
class WeatherRepoImpl(private val api: Api):WeatherRepo{
    override suspend fun searchLocation(query: String): BaseModel<List<Location>> {
        //The api.getDailyForecasts(locationKey = locationKey) is passed as a lambda expression to
        // the request function as the parameter
        // In Kotlin, when the last parameter of a function is a lambda, you can move it outside the
        // parentheses, which allows you to use a more concise syntax. This is often referred to as
        // trailing lambda syntax.
        //so basically it is returning the returned value of request function,
        // which is basemodel, which match the function return type baseModel<List<Location>>
       return request{
           api.searchLocation(query = query)
       }
    }

    override suspend fun getDailyForecasts(locationKey: String): BaseModel<DailyForcasts> {
        return request{
           api.getDailyForecasts(locationKey = locationKey)
        }
    }

    override suspend fun getHourlyForecasts(locationKey: String): BaseModel<List<HourlyForecast>> {
        return request{
            api.getHourlyForecasts(locationKey=locationKey)
        }
    }
}
//takes a single parameter named request
// This parameter is a lambda function that is marked as suspend and returns a Response<T>
//Response<T> type is associated with retrofit library
//Retrofit uses Response<T> to represent the result of an HTTP request, where T is the
// expected type of the response body.

//SO the request is the lamba funtion parameter that returns http Response and
// the whole function will use this returned value of this lamba function to
// wrap to the basemodel and return the baseModel<T> type
suspend fun <T>request(request:suspend()-> Response<T>):BaseModel<T>{
    try{
        //he lambda function passed as a parameter is called using request().
        // The result is then processed using the also extension function.
        request().also{
            //check if http request is successful
            return if(it.isSuccessful){
                //body means the actual data that the server is providing in response
                // in JSon to the request.
                // !! is the Kotlin "not-null assertion" operator, which asserts that the value is non-null.
                //'m sure this won't be null, and I'm willing to risk a NullPointerException if it is."
                //the return type of ths it.body()!! depends on what data structure does this retrieve from response
                //look at the DailyForcasts's class's comment for more information, you will get it
                BaseModel.Success(it.body()!!)
            }else{
                //it.errorBody()--->This retrieves the error body of the HTTP response in JSon.
                //?-->check if it is null, if not, call the String() to obtain the error message as astring
                //then the ? checks if the result of string() is null, if not, convert it to string.
                BaseModel.Error(it.errorBody()?.string().toString())
            }
        }
        //After handling the exception, the program will continue executing the subsequent code.
    }catch(e:Exception){
        return BaseModel.Error(e.message.toString())
    }
}