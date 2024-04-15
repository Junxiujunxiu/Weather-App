package com.example.weatherjetpack

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherjetpack.screens.HomeScreen
import com.example.weatherjetpack.screens.WeatherScreen

@Composable
    fun Navigation(){
        //ensures that the NavController is preserved across recompositions and
        // configuration changes, allowing for smooth navigation and maintaining the navigation state.
        //because the UI is typically rebuilt in response to changes in the application's state
    val navController = rememberNavController()
    //NavHost is a composable that set up a navigation graph, specifying the navigation
    // controller and the start destination.
    NavHost(navController = navController, startDestination = "home"){
        //When the navigation reaches the "home" destination, the HomeScreen composable function will be invoked.
        composable("home"){
            //passed the navController as a parameter.
            //his allows the HomeScreen to interact with the navigation system, such as navigating to other destinations.
            HomeScreen(navController = navController)
        }
        //composable function associated with a dynamic destination path
        //When the navigation reaches a destination with a path like "weather/123/NewYork/US", this composable will be invoked
        // and the WeatherScreen composable function will be called.
        //arguments: This is a list of navArgument objects, specifying the expected arguments for the destination.

        composable("weather/{location_key}/{name}/{country}", arguments = listOf(
            //this list  define both the arguments and their types for the "weather/{location_key}
            // /{name}/{country}" destination in Jetpack Compose navigation.
            //"location_key" parameter should be of type String
            navArgument("location_key") {
                type = NavType.StringType
            },
            //"name" parameter should be of type String
            navArgument("name") {
                type = NavType.StringType
            },
            //"country" parameter should be of type String
            navArgument("country") {
                type = NavType.StringType
            }
        )){
            //composable function associated with the "weather/{location_key}/{name}/{country}" destination.
            //extracting the values of the "location_key," "name," and "country" arguments from
            // the NavBackStackEntry and assigning them to variables locationKey, locationName,
            // and country
            WeatherScreen(
                navController = navController,// navigation-related interactions.
                //it---> NavBackStackEntry ---->he entry in the back stack corresponding to the
                // "weather/{location_key}/{name}/{country}" destination, and it.arguments allows
                // you to access the arguments associated with that destination.
                locationKey = it.arguments?.getString("location_key")?:"",//if not null, getString("location_key") will return its value
                locationName = it.arguments?.getString("name")?:"",
                country = it.arguments?.getString("country")?:""
            )
        }
    }

}