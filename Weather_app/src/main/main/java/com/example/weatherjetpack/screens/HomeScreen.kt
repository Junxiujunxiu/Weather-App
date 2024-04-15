package com.example.weatherjetpack.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherjetpack.models.BaseModel
import kotlinx.coroutines.delay

//the viewmodel can keep the data across the configuration change and can be observed.
//see the HomeviewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // this parameter is used for navigating from the HomeScreen to other screens within the app
    navController:NavController,
    //= viewModel(0 is a default one that trigger when no viewmodel is passed.
    viewModel: HomeViewModel = viewModel()
) {
    //locations by---> this variable will be updated based on the state change.
    //viewModel.locations--->represents a Flow of data that is likely being emitted by a ViewModel
    //collectAsState()--->collect values emitted by a Flow and automatically update the state of the
    // Compose UI component whenever the Flow emits a new value.
    val locations by viewModel.locations.collectAsState()

    //declare multiple variables at once by extracting values from a structure.
    //first one is the current state and second one is a function used to update the state
    val (city, setCity) = remember {
        //creates a state variable called city with an initial value of an empty string ("").
        mutableStateOf("")
    }

    //the coroutine will be launched whenever the value of city changes.
    //LaunchedEffect is a Compose effect that launches a coroutine
    // the entire LaunchedEffect block represents a coroutine that gets launched by Compose.
    //the LaunchedEffect itself is a coroutine builder
    //it avoids blocking the main thread, but it still runs on the main thread
    //so it is like concurrent task.
    LaunchedEffect(city) {
        // a delay in the coroutine
        delay(500)
        if (city.isNotEmpty()) {
            // initiating a search based on the non-empty city name.
            viewModel.searchLocation(city)
        }
    }

    Column(
        modifier = Modifier
            //column
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        //app text
        Text(
            text = "Welcome to weather app.",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        //space between text and the box
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                //black container height
                .height(55.dp)
                //boundary roundness
                .clip(RoundedCornerShape(8.dp))
                //black container colour, alignment, default text, textchange-->setCity(it), textfield colour
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            //city is null in the beginning and the onValueChange will take the user inout and update it.
            //added fillMaxWidth to clear the empty space
            TextField(modifier = Modifier.fillMaxWidth(),value = city, onValueChange = {
                setCity(it)
            }, colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
                //set the text color in the text field
            ), textStyle = TextStyle(color = Color.White),
                placeholder = {
                Text("City")
            })
        }
        //space between search bar and "Choose your city"
        Spacer(modifier = Modifier.height(32.dp))
        //AnimatedVisibility composable is used to control the visibility of its content with enter and exit animations.
        //. If locations is of type baseModel.Success, the content will be visible; otherwise, it will be invisible.
        AnimatedVisibility(
            visible = locations is BaseModel.Success,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            //the content that will be displayed inside the Column when it is visible.
            Column {
                Text(text = "Choose your city:", color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                //when---> decision-making structure. It checks the type of the locations variable like switch
                //is baseModel.Success -> { ... }:--->if the type of locations is baseModel.Success.
                //If it is, the code inside the following block runs.
                when (val data = locations) {
                    /*If the network request or data retrieval operation is successful, it will wrap the actual data
                    (e.g., a list of locations) in a baseModel.Success instance.*/
                    //like in switch, if the locations = baseModel.Success
                    is BaseModel.Success -> {
                        // create a grid with a fixed number of columns (2 in this case).
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            //iterate over the data(second one) property of the baseModel.Success.
                            // This assumes that data has a data property containing a list.
                            //For each location in the list, a Compose Row is created. It has a
                            // fixed height, rounded corners, and a background color
                            items(data.data) { location ->
                                // create a horizontal layout where its children are placed next to each other horizontally.
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.secondary)
                                    /*clickable modifier is used to make the row clickable.
                                     When clicked, it navigates to a destination using the navController.navigate function. */
                                    .clickable {
                                        navController.navigate("weather/${location.key}/${location.englishName}/${location.country.englishName}")
                                    }
                                    .padding(8.dp),
                                    // space is distributed equally between the child elements.
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    //Aligns child elements vertically in the center of the Row
                                    //this column is contained by the row above,
                                    verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        //city name in the column
                                        Text(
                                            location.englishName,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        //country name in the column
                                        Text(
                                            location.country.englishName,
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    else -> { // Code inside this block runs if locations is not a success}
                    }
                }
            }
            AnimatedVisibility(
                visible = locations is BaseModel.Loading,
                enter = fadeIn() + scaleIn(),//loading start animation
                exit = fadeOut() + scaleOut()//loading end animation
            ) {
                CircularProgressIndicator(color = Color.White)//displays a circular, indeterminate loading indicator.
            }
        }
    }
}