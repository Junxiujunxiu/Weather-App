package com.example.weatherjetpack.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherjetpack.models.DailyForcasts
import com.example.weatherjetpack.models.HourlyForecast
import com.example.weatherjetpack.models.BaseModel
import com.example.weatherjetpack.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherViewModel: ViewModel(),KoinComponent {
    //inject the interface
    private val repo:WeatherRepo by inject()
    //indicates that the initial state for _hourlyForecast is set to a loading
    // state. It suggests that the application is currently fetching or waiting for data, and this loading state is the initial state.
    private val _hourlyForecast: MutableStateFlow<BaseModel<List<HourlyForecast>>> = MutableStateFlow(BaseModel.Loading)
    val hourlyForecast = _hourlyForecast.asStateFlow()

    private val _dailyForecast:MutableStateFlow<BaseModel<DailyForcasts>> = MutableStateFlow(BaseModel.Loading)
    val dailyForecast = _dailyForecast.asStateFlow()


        fun getDailyForecast(locationKey: String) {
            viewModelScope.launch {
                repo.getDailyForecasts(locationKey).also { data ->
                    _dailyForecast.update { data }
                }
            }
        }

            fun getHourlyForecast(locationKey:String) {
                viewModelScope.launch {
                    repo.getHourlyForecasts(locationKey).also { data ->
                        _hourlyForecast.update { data }
                    }
                }
        }
    }
