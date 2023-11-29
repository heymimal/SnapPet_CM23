package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun BottomNavigationBar7() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        Settings()

        //Text(text = "Hello")
    }
}


@Composable
fun Settings(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize())


        Text(
            text = "Settings",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 0.dp,
                    y = 29.dp
                ))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 28.dp,
                    y = 386.dp
                )
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Report Problem",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 45.dp,
                    y = 402.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.report_problem_icon),
            contentDescription = "image 10",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(
                    x = 275.dp,
                    y = 16.dp
                )
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 28.dp,
                    y = 266.dp
                )
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Customer Support",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 44.dp,
                    y = 281.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.customer_support_icon),
            contentDescription = "image 9",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(
                    x = 279.dp,
                    y = (-104).dp
                )
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 146.dp
                )
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Change Language",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 44.dp,
                    y = 161.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.language_icon),
            contentDescription = "image 8",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(
                    x = 275.dp,
                    y = (-224).dp
                )
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 640.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)){ }
        Text(
            text = "Back",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 43.dp,
                    y = 654.dp
                ))
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    BottomNavigationBar7()
}