package com.example.snappet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.snappet.R
import com.example.snappet.navigation.Screens

@Composable
fun MonthAnimalNav(navController: NavHostController, presentMonthAnimal: String) {
    // Map the month animals to corresponding image resources
    val animalImageResources = mapOf(
        "Horse" to R.drawable.horse,
        "Lizard" to R.drawable.lizard,
        "Peacock" to R.drawable.peacock,
        "Pig" to R.drawable.pig,
        "Rabbit" to R.drawable.rabbit,
        "Sheep" to R.drawable.sheep,
        "Bird" to R.drawable.bird,
        "Cat" to R.drawable.cat,
        "Chicken" to R.drawable.chicken,
        "Cow" to R.drawable.cow,
        "Dog" to R.drawable.dog,
        "Duck" to R.drawable.duck
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp)
            .wrapContentSize(align = Alignment.Center)
    ) {
        // Title: "Month of the:"
        Text(
            text = "Month of the:",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Image using the Image composable
        val animalImageResource = animalImageResources[presentMonthAnimal]
        if (animalImageResource != null) {
            Image(
                painter = painterResource(id = animalImageResource),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp) // Adjust the size as needed
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }

        // Text with presentMonthAnimal
        Text(
            text = presentMonthAnimal,
            style = TextStyle(fontSize = 24.sp, color = Color.Black),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Description
        Text(
            text = "Take pictures of this animal to get a x5 points multiplier!",
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.Center)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Button to navigate to Trophies Info
        Button(
            onClick = {
                navController.navigate(route = Screens.Home.route)
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .wrapContentSize(align = Alignment.Center)
        ) {
            Text(text = "Next", style = TextStyle(fontSize = 24.sp))
        }
    }
}