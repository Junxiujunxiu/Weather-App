package com.example.weatherjetpack.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherjetpack.models.Location
import com.example.weatherjetpack.models.BaseModel
import com.example.weatherjetpack.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//The ViewModel class provides a way to maintain data
// across the lifecycle of the UI component without having to worry about configuration changes
//KoinComponent----> dependency injection
class HomeViewModel : ViewModel(), KoinComponent {
    // Injecting WeatherRepo using Koin's inject() function
    //it will also implement the weatherRepoimpl, because i bind the WeatherRepo and WeatherRepoimpl in App
    //class with dependency injection
    val repo: WeatherRepo by inject()

    // MutableStateFlow to represent the state of the locations data
    //Think of it as a container that holds a value.
    //The "mutable" part means you can change (mutate) the value inside the container over time.
    //the ViewModel can update this state flow based on various events (loading, success, error),
    //so this is the source of data
    private val _locations: MutableStateFlow<BaseModel<List<Location>>?> = MutableStateFlow(null)

    // Public StateFlow to expose the locations data to observers
    //asStateFlow is the member function of the MutableStateFlow
    /*The purpose of asStateFlow() is to convert the mutable state flow
     (MutableStateFlow) into an immutable state flow (StateFlow). The resulting
     StateFlow can be observed, but you can no longer modify its state directly.
    The new variable locations is of type StateFlow<baseModel<List<Location>>?>.
    This means that it represents a flow of data of the specified type
    (baseModel<List<Location>>?), and it can be observed by collectors without
     allowing direct modifications.
     n summary, this line of code creates an immutable state flow (locations) based on the existing mutable state flow (_locations). */
    //and this is a StateFlow that other components can observe
    val locations = _locations.asStateFlow()

    // Function to perform a location search
    fun searchLocation(query: String) {
        viewModelScope.launch {
            // Update the _locations StateFlow to indicate that data is loading
            _locations.update { BaseModel.Loading }
            // Call the searchLocation function in the repository
            // Update the _locations StateFlow with the result of the search
            //also function is called on the result.

            //data here is the returnd value of repo.searchLocation(query)
            //which is baseModel<List<Location>>
            repo.searchLocation(query).also { data ->
                // updates the _locations state flow by setting its value to the received data (data).
                _locations.update { data }
            }
        }
    }
}