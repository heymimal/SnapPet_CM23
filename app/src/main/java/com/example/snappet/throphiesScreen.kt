package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar8() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        Trophies()

        //Text(text = "Hello")
    }
}



@Composable
fun Trophies(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 800.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 800.dp)
                    .background(color = Color.White))

        }
        Text(
            text = "Missions and Trophies",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 29.dp,
                    y = 33.dp
                ))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 31.dp,
                    y = 126.dp
                )
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 0.dp,
                        y = 43.dp
                    )
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.5.dp,
                        y = 0.dp
                    ))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.dp,
                        y = 89.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 29.dp,
                    y = 298.dp
                )
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 0.dp,
                        y = 43.dp
                    )
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 cat photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.dp,
                        y = 0.dp
                    ))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.dp,
                        y = 89.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 36.dp,
                    y = 470.dp
                )
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 0.dp,
                        y = 43.dp
                    )
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 dog photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.dp,
                        y = 0.dp
                    ))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 0.dp,
                        y = 89.dp
                    ))
        }
    }
}

@Preview
@Composable
private fun TrophiesPreview() {
    BottomNavigationBar8()
}