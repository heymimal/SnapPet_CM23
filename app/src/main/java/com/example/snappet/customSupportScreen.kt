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
fun BottomNavigationBar2() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        CostumerSupport()

        //Text(text = "Hello")
    }
}



@Composable
fun CostumerSupport(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = "Customer Support",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.dp,
                    y = 29.dp))
        Image(
            painter = painterResource(id = R.drawable.support),
            contentDescription = "image 11",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.dp,
                    y = 148.dp)
                .requiredSize(size = 144.dp))
        Text(
            text = "Telephone: 911111111",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 34.dp,
                    y = 385.dp))
        Text(
            text = "Email: snapet@gmail.com",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 34.dp,
                    y = 468.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 640.dp)

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
                    .offset(x = 18.dp,
                        y = 14.dp))
        }
    }
}

@Preview
@Composable
private fun CostumerSupportPreview() {
    BottomNavigationBar2()
}