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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar5() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        PhotoForms()

        //Text(text = "Hello")
    }
}


@Composable
fun PhotoForms(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 295.dp
                )
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 308.dp)
                    .requiredHeight(height = 61.dp)
                    .background(color = Color(0xffd9d9d9)))
            Text(
                text = "Animal",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 19.dp,
                        y = 15.dp
                    ))
            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = "Polygon 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 273.dp,
                        y = 19.dp
                    )
                    .requiredWidth(width = 23.dp)
                    .requiredHeight(height = 26.dp)
                    .rotate(degrees = 180f))
        }
        Image(
            painter = painterResource(id = R.drawable.imagemforms),
            contentDescription = "image 13",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = (-0.5).dp,
                    y = 23.dp
                )
                .requiredWidth(width = 325.dp)
                .requiredHeight(height = 228.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 28.dp,
                    y = 638.dp
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
                        x = 19.dp,
                        y = 14.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 250.dp,
                    y = 638.dp
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
                text = "Save",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 20.dp,
                        y = 14.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 28.dp,
                    y = 385.dp
                )
                .requiredWidth(width = 319.dp)
                .requiredHeight(height = 121.dp)
        ) {
            Text(
                text = "Context",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 7.dp,
                        y = 46.dp
                    )
                    .requiredWidth(width = 217.dp)
                    .requiredHeight(height = 30.dp)
            ) {
                Text(
                    text = "Entertainment",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 50.dp,
                            y = 0.dp
                        ))
                val checkedState = remember { mutableStateOf(true) }
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 3.dp
                        ))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 7.dp,
                        y = 91.dp
                    )
                    .requiredWidth(width = 312.dp)
                    .requiredHeight(height = 30.dp)
            ) {
                Text(
                    text = "Animal in need of help",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 50.dp,
                            y = 0.dp
                        ))
                val checkedState = remember { mutableStateOf(true) }
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 5.dp
                        ))
            }
        }
    }
}

@Preview
@Composable
private fun PhotoFormsPreview() {
    BottomNavigationBar5()
}