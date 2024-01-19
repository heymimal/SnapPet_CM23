package com.example.snappet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
        }
    }
}


@Composable
fun Trophies(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Missions and Trophies",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .offset(y = 29.dp)
        )

        Spacer(modifier = Modifier.height(150.dp))

        Mission(name = "Take 10 photos!", progress = 7, total = 10)
        Mission(name = "Take 10 cat photos!", progress = 3, total = 10)
        Mission(name = "Take 10 dog photos!", progress = 2, total = 10)
        Mission(name = "Take 50 photos!", progress = 7, total = 50)
    }
}

@Composable
fun Mission(name: String, progress: Int, total: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = progress.toFloat() / total.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            color = if (progress == total) Color.Green else Color.Blue
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "$progress/$total")
    }

    Spacer(modifier = Modifier.height(30.dp))

}

/*@Preview
@Composable
private fun TrophiesPreview() {
    Trophies_nav()
}*/

/*
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
 */