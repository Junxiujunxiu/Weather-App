package com.example.weatherjetpack.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.ArrowDownward
import androidx.compose.material.icons.sharp.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherjetpack.models.BaseModel
import com.example.weatherjetpack.ui.theme.russoFont
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun WeatherScreen(
    navController: NavController,
    locationKey: String,
    locationName: String,
    country: String,
    viewModel: WeatherViewModel = viewModel()
) {

    //collect the state emitted by view model and automatically
    // recompose the current UI everytime the data changes
    val dailyForecasts by viewModel.dailyForecast.collectAsState()
    val hourlyForecasts by viewModel.hourlyForecast.collectAsState()

    //coroutine
    //side effect, unit means it only executes once in a lifetime
    //This ensures that the necessary data is fetched
    //then the collectAsState() above will reflect the UI change
    LaunchedEffect(Unit) {
        viewModel.getDailyForecast(locationKey)
        viewModel.getHourlyForecast(locationKey)
    }

    //screen padding
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.padding(vertical = 32.dp)) {
            //back icon
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        // move backwards
                        navController.popBackStack()
                    },
                tint = Color.White,
                contentDescription = null,
            )
            // space horizontally(row)
            Spacer(modifier = Modifier.width(10.dp))
            //first text vertically (column), Auckland
            Column {
                Text(
                    text = locationName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                //second text vertically (column),
                Text(text = country, color = Color.Gray)
            }
        }//reference : val hourlyForecasts by viewModel.hourlyForecast.collectAsState()
        //visible is true when it successes
        AnimatedVisibility(visible = hourlyForecasts is BaseModel.Success) {
            //typecast
            //the reason that i need to do the type cast is that when i run the
            //program, if it is successful, it it will only create an instance of
            //baseModel.Success for hourlyForecasts, but the original type still remains
            //as baseModel<List<HourlyForecast>>, that is why i need to do the type cast
            val data = hourlyForecasts as BaseModel.Success
            //first data is an instance of baseModel.Success
            //second data is accessing the data property
            //last i will show the first element in the temperature list
            val temp = data.data.first().temperature
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                //main degree number in the middle
                Text(
                    text = "${temp.value}°",
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp,
                    color = Color.White,
                    fontFamily = russoFont
                )
            }
        }
        //loading anime
        //it will show the loading first then it show the baseModel.success.
        AnimatedVisibility(visible = hourlyForecasts is BaseModel.Loading) {
            Loading()
        }

        Spacer(modifier = Modifier.height(16.dp))
        //hourly forcast
        Text(
            "Hourly Forecasts:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        //
        AnimatedVisibility(visible = hourlyForecasts is BaseModel.Success) {
            val data = hourlyForecasts as BaseModel.Success
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                //iterate over the data---> hourly forcast list
                items(data.data) { forecast ->
                    //each column container that contains time + icon + degree
                    Column(
                        modifier = Modifier
                            .size(100.dp, 140.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        //arrangement for the element inside the column
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        //hourly forcast date format
                        Text(
                            //H -- hour, a ---am/pm
                            //SimpleDateFormat--format date as strings
                            //Date(forecast.epochDateTime * 1000) is indeed creating a Date object representing
                            // a specific point in time. so it is epochDateTime element in JSon * 1000
                            text = SimpleDateFormat("H a").format(Date(forecast.epochDateTime * 1000)),
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        //icon image
                        AsyncImage(
                            modifier = Modifier.size(70.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://developer.accuweather.com/sites/default/files/${forecast.weatherIcon.fixIcon()}-s.png")
                                .build(),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = forecast.temperature.value.toString() + "°",
                            color = Color.White
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = hourlyForecasts is BaseModel.Loading) {
            Loading()
        }
        Spacer(modifier = Modifier.height(16.dp))
        //daily forcast
        Text(
            "Daily Forecasts:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(visible = dailyForecasts is BaseModel.Success) {
            val data = dailyForecasts as BaseModel.Success
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data.data.dailyForecasts) { forecast ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(start = 16.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //dailyforcast format
                        Text(
                            text = "${SimpleDateFormat("d").format(Date(forecast.epochDate*1000))}th",
                            color = Color.White
                        )
                        //down arrow + min degree +  up arrow + max degree
                        Row {
                            Icon(Icons.Sharp.ArrowDownward, tint = Color(0xffff5353), contentDescription = null)
                            Text(text = "${forecast.temperature.min.value}°", color = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Sharp.ArrowUpward, tint = Color(0xff2eff8c), contentDescription = null)
                            Text(text = "${forecast.temperature.max.value}°", color = Color.White)
                        }
                        //dailyforcast icon
                        AsyncImage(
                            modifier=Modifier.size(70.dp),
                            model = ImageRequest.Builder(LocalContext.current)
                                //s.png--small version of the image
                                //forecast.day.icon---this code generate dynamic icon for some reason---check the accue weather Api
                                .data("https://developer.accuweather.com/sites/default/files/${forecast.day.icon.fixIcon()}-s.png")
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        //loadidng added---so all the loadings will show when the hourly forcast is loading
        AnimatedVisibility(visible = hourlyForecasts is BaseModel.Loading) {
            Loading()
        }
    }
}

//extension function for the 'Int' type
//means it can be called on any integer value and returns a string
// ensure that an integer representing a value like an hour or minute is always formatted with two digits
fun Int.fixIcon(): String {
    //if this is >9, meaning it has two or more digits
    //in this case, it will convert the integer to a string
    return if (this > 9) {
        toString()
    } else {
        // if the integer is 5, it will return "05". If it's 2, it will return "02".
        "0${this}"
    }
}

//loading indicator customization
@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.White)
    }
}