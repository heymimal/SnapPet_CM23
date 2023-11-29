package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
fun BottomNavigationBar6() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        ReportProblem()

        //Text(text = "Hello")
    }
}


@Composable
fun ReportProblem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = "Report Problem",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 0.5.dp,
                    y = 26.dp
                ))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 640.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                modifier = Modifier
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
                        x = 18.dp,
                        y = 14.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 246.dp,
                    y = 640.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                modifier = Modifier
                    .requiredWidth(width = 84.dp)
                    .requiredHeight(height = 52.dp)){ }
            Text(
                text = "Report",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(
                        x = 1.dp,
                        y = 14.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 0.dp,
                    y = 296.dp
                )
                .requiredWidth(width = 332.dp)
                .requiredHeight(height = 295.dp)
                .background(color = Color(0xffd9d9d9)))
        Text(
            text = "Report your problem here, we\nwill contact you as soon as\npossible.",
            color = Color(0xff707070),
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 34.dp,
                    y = 326.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.problem),
            contentDescription = "image 12",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 0.5.dp,
                    y = 90.dp
                )
                .requiredWidth(width = 177.dp)
                .requiredHeight(height = 173.dp))
    }
}

@Preview
@Composable
private fun ReportProblemPreview() {
    BottomNavigationBar6()
}