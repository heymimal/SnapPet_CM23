package com.example.snappet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.snappet.LoginStreakViewModel
import com.example.snappet.navigation.Screens

@Composable
fun loginStreakNav(
    loginStreakViewModel: LoginStreakViewModel,
    navController: NavController,
    updatedPoints: Int,
    testeLoginStreak: Int
){
    //val loginStreakDataState by loginStreakViewModel.loginStreakData.observeAsState()
    val loginStreakDataState = testeLoginStreak
    val snaPointsDataState by loginStreakViewModel.snaPointsData.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            //text = "Login Streak+LOGINSTREAK:$testeLoginStreak-PONTOS:$updatedPoints",
            text = "Login Streak:",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .offset(y = 29.dp)
        )

        // Days and points
        val daysInfo = listOf(
            DayInfo("Day: 1", 5),
            DayInfo("Day: 2", 5),
            DayInfo("Day: 3", 5),
            DayInfo("Day: 4", 10),
            DayInfo("Day: 5", 10),
            DayInfo("Day: 6", 10),
            DayInfo("Day: 7", 25)
        )

        Spacer(modifier = Modifier.height(200.dp))

        // Group days into separate lists
        val groupedDays = daysInfo.chunked(3)

        // Display days in rows
        groupedDays.forEach { days ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                days.forEach { dayInfo ->
                    val boxColor = when {
                        dayInfo.day.contains(loginStreakDataState.toString()) -> Color.Blue // Blue for the matching day
                        dayInfo.day.substringAfter(":").trim().toIntOrNull() ?: 0 < loginStreakDataState ?: 0 -> Color.Yellow // Yellow for lower values
                        else -> Color.Gray // Gray for higher values or when loginStreakDataState is null
                    }

                    val blue = Color(0xFF0077FF)
                    val lighterOrange = Color(0xFFFFAA80)
                    val orange = Color(0xFFFF8800)

                    // Inside the Box composable
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(100.dp)
                            .background(
                                when {
                                    dayInfo.day.substringAfter(":").trim().toIntOrNull() ?: 0 == loginStreakDataState -> orange
                                    dayInfo.day.substringAfter(":").trim().toIntOrNull() ?: 0 < loginStreakDataState ?: 0 -> lighterOrange
                                    else -> Color.Gray
                                },
                                shape = RoundedCornerShape(8.dp) // Adjust the corner radius as needed
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = dayInfo.day, color = Color.White)
                            Text(text = "Points: ${dayInfo.points}", color = Color.White)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                navController.navigate(route = Screens.Home.route)
                      },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 246.dp,
                    y = 680.dp
                )
                .height(50.dp)
                .width(100.dp)

        )
        {
            Text(text = "Next", style = TextStyle(fontSize = 20.sp))
        }
    }
}

data class DayInfo(val day: String, val points: Int)
@Preview
@Composable
private fun LoginPreview() {
    //loginStreak(Modifier)
}