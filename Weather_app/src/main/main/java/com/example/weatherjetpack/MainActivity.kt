package com.example.weatherjetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherjetpack.ui.theme.WeatherJetpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { //same as setContentView -- assign xml to main activity
            //but here in setContent -- we difine our composibles
            //the view in XML is now considered a composible
            WeatherJetpackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    // specify the size and background styling for the Surface
                    //background-->chaining function that adds a background to the composable
                    //Brush.verticalGradient(...)--> create a vertical gradient brush for the background.
                    modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(
                        //specifies the colors to be used in the gradient.
                        listOf(
                            //the color is defined in the Theme class
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                        )
                    )),
                    color = Color.Transparent
                ) {
                    Navigation()
                }
            }
        }
    }
}

/*
//nothing more than a kotlin function
@Composable
//the name parameter is state
//as well as the state changes, our text will change.
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        //modifier allows you to apply various styling and layout modifications to
        // the Text composable. For example, you can use Modifier.padding(...) to add padding,
    modifier = modifier
    )
}

//This allows you to see a visual representation of how the greeting UI looks in the Android Studio preview pane.
@Preview(showBackground = true)
@Composable
//generates a preview of the UI defined within this function
//it is declaritive way
fun GreetingPreview() {
    WeatherJetpackTheme {
        Greeting("Android")
    }
}*/