package com.example.snappet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.snappet.navigation.Navigation
import androidx.compose.foundation.lazy.LazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrophiesInfoNav(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Navigation(navController = navController,3)
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Text(
                        text = "Trophies Information",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Trophies can be gained through completing various challenges. " +
                                "The highest Trophy will be shown in your profile.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Start)
                    )
                    Text(
                        text = "Trophies differ in rarity:",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Bronze Trophies", trophyType = "bronze", modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Common and fairly easy to obtain.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Silver Trophies", trophyType = "silver", modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Moderate effort to obtain, these require some time.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)  // Set vertical padding to 0.dp
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Gold Trophies", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Hard to obtain, these require a lot of time and effort.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Legendary Trophies", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Truly exceptional and extremely rare.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {
                    Text(
                        text = "Trophy List",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item {
                    RaritySquare(text = "Snappy Snapper", trophyType = "bronze",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 50 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Animal Explorer", trophyType = "bronze",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 100 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Pet Paparazzo", trophyType = "bronze",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 150 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Nature Scout", trophyType = "bronze",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 200 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Wildlife Rookie", trophyType = "bronze",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 250 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Silver Safari Scout", trophyType = "silver",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 300 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Lens Warrior", trophyType = "silver",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 350 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Animal Whisperer", trophyType = "silver",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 400 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Nature Enthusiast", trophyType = "silver",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 450 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Silver Snapshot Master", trophyType = "silver",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 500 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Gold Wilderness Conquerer", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 550 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Shutter Champion", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 600 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Creature Adventurer", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 650 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Gold Wildlife Expert", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 700 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Gold Photo Virtuoso", trophyType = "gold",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 750 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Legendary Nature Maestro", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 800 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Platinum Safari Maestro", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 850 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "Supreme Wildlife Photographer", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 900 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "SnaPet Virtuoso", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 950 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                }
                item {
                    RaritySquare(text = "SnaPet Master", trophyType = "legendary",modifier = Modifier.padding(start = 16.dp, bottom = 0.dp))
                    Text(
                        text = "Receive 1000 SnaPoints or more.",
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 0.dp)
                            .align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

//how a trophy is presented (colour and text)
@Composable
fun RaritySquare(text: String, trophyType: String, modifier: Modifier = Modifier) {
    val squareColor = when (trophyType) {
        "bronze" -> Color(0xFFCD7F32)
        "silver" -> Color.Gray
        "gold" -> Color(0xFFFFD700)
        "legendary" -> Color(0xFFE2590B)
        else -> Color.Black
    }
    val textColor = if (trophyType == "gold") Color.Black else Color.White
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(squareColor, shape = RoundedCornerShape(8.dp)) // Rounded corners
                .padding(8.dp)
        ) {
            Text(
                text = text,
                style = TextStyle(fontSize = 16.sp, color = textColor)
            )
        }
    }
}