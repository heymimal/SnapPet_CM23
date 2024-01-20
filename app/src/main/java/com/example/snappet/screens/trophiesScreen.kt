package com.example.snappet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.snappet.data.DailyMission
import com.example.snappet.navigation.Navigation
import com.example.snappet.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrophiesNav(navController: NavHostController, dailyMissions: List<DailyMission>) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Navigation(navController = navController)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)
            ) {
                Text(
                    text = "Throphies and Missions",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                LazyColumn {
                    itemsIndexed(dailyMissions) { index, mission ->
                        MissionCard(mission = mission)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Throphies Info button placed after missions
                    item {
                        Button(
                            onClick = {
                                navController.navigate(route = Screens.TrophiesInfo.route)
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 16.dp)
                        ) {
                            Text(text = "Throphies Info", style = TextStyle(fontSize = 20.sp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MissionCard(mission: DailyMission) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = mission.missionDescription,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Status: ${mission.userProgress}/${mission.goal}",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Points: ${mission.points}",
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (mission.completed) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xffe2590b))
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "COMPLETED!",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}