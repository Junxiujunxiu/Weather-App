package com.example.weatherjetpack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

//for dark mode
private val DarkColorScheme = darkColorScheme(
    primary = Dark200,
    secondary = Dark500,

)
//for light mode
private val LightColorScheme = lightColorScheme(
    primary = Dark200,
    secondary = Dark500
)

@Composable // mark the function as composable
//They define a part of the UI that can be recomposed efficiently when the underlying data changes.
fun WeatherJetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    //content of the theme
    //it is  composable function that returns void(unit)
    //it is a function parameter
    //jetpack compose style to indicate function type ()->Unit
    content: @Composable () -> Unit
) {
    //
    val colorScheme = when {
       // if dynamicColor is true and the Android version is equal to or greater than Android 12
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // if darkTheme is true, it sets the colorScheme to DarkColorScheme,
        // otherwise, it sets it to LightColorScheme
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    //get the current view within the composable
    val view = LocalView.current
    //if it is not in edit mode
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    //hide the status bar in your Android app using Jetpack Compose.
        rememberSystemUiController().isStatusBarVisible = false

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

