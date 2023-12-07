package com.example.snappet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.snappet.Navigation
import com.example.snappet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun menuBottomNav(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        ThreeByThreeGrid1()

        //Text(text = "Hello")
    }
}

@Composable
fun CardWithImageAndText(imageVector: ImageVector, text: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ThreeByThreeGrid1() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First Row: "Animais"
        item {
            Column {
                Text("Animais", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock") }
                }
            }
        }


        // Second Row: "Most Recent Photos"
        item {
            Column {
                Text("Recent Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog1") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock1") }
                }
            }
        }

        item {
            Column {
                Text("Most Popular Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat2") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock2") }
                }
            }
        }

        item {
            Column {
                Text("Dogs", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Cat2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Cat3") }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Dog1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Dog3") }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Peacock1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Dog3") }
                }
            }
        }


    }
}