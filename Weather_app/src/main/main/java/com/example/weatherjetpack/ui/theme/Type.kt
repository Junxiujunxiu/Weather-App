package com.example.weatherjetpack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weatherjetpack.R
// create a list containing a single Font instance
//i can add it more in the future.
val ubuntuFont = FontFamily(listOf(
    Font(R.font.ubuntu)
))

val russoFont = FontFamily(listOf(
    Font(R.font.russo_one)
))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ubuntuFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)