package com.example.snappet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.snappet.data.Photo
import com.example.snappet.navigation.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun PhotoDetailScreen(photo: Photo, navController: NavController) {
    // Use a Column to arrange the information vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the photo with a fixed height
        val imageUrl = if (photo.sender != Firebase.auth.currentUser?.uid) {
            photo.downloadUrl
        } else {
            photo.imageUri.toString()
        }

        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        // Add some space between the photo and the text information
        Spacer(modifier = Modifier.height(16.dp))

        // Display the animal type
        Text(
            text = "Animal Type: ${photo.animalType}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        // Add more space
        Spacer(modifier = Modifier.height(8.dp))

        // Display the description
        Text(
            text = "Description: ${photo.description}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {navController.navigate(Screens.Home.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Back")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailCard(photo: Photo) {
    var expanded by remember {mutableStateOf(false)}
    val imageUrl = if (photo.sender != Firebase.auth.currentUser?.uid) {
        photo.downloadUrl
    } else {
        photo.imageUri.toString()
    }
    // Get the local configuration
    val configuration = LocalConfiguration.current
    val smallAmp = 1
    val largeAmp = 3
    val smallWidth = 120.dp
    val smallHeight = 150.dp
    val largeWidth = configuration.screenWidthDp.dp
    val largeHeight = 450.dp

    var width by remember {
        mutableStateOf(smallWidth)
    }
    var height by remember {
        mutableStateOf(smallHeight)
    }
    var amp by remember {
        mutableIntStateOf(smallAmp)
    }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = width, height = height),
        onClick = {
            if(expanded){
                width = smallWidth
                height = smallHeight
                amp = smallAmp
            } else {
                width = largeWidth
                height = largeHeight
                amp = largeAmp
            }
            expanded = !expanded
                  },
    ) {
        Spacer(modifier = Modifier.height(3.dp))
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height((75*amp).dp)
                .clip(MaterialTheme.shapes.medium)
        )

        // Add some space between the photo and the text information
        Spacer(modifier = Modifier.height(3.dp))
        Row(){
            Text(
                text = "Animal:",
                fontWeight = FontWeight.Bold,
                fontSize = (10*amp).sp
            )
            Text(
                text = " ${photo.animalType}",
                fontSize = (10*amp).sp
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Description: ",
            fontWeight = FontWeight.Bold,
            fontSize = (10*amp).sp
        )
        Spacer(modifier = Modifier.height(3.dp))
        // Display the description
        Text(
            text = "${photo.description}",
            fontSize = (10*amp).sp
        )
    }
}

@Composable
fun LargeCard(photo: Photo, imageUrl: String){
    val modifier = 3
    Spacer(modifier = Modifier.height(3.dp))
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height((75*3).dp)
            .clip(MaterialTheme.shapes.medium)
    )

    // Add some space between the photo and the text information
    Spacer(modifier = Modifier.height(3.dp))
    Row(){
        Text(
            text = "Animal:",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
        Text(
            text = " ${photo.animalType}",
            fontSize = 10.sp
        )
    }
    Spacer(modifier = Modifier.height(3.dp))
    Text(
        text = "Description: ",
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    )
    Spacer(modifier = Modifier.height(3.dp))
    // Display the description
    Text(
        text = "${photo.description}",
        fontSize = 10.sp
    )
}

@Composable
fun smallCard(photo: Photo,imageUrl: String){
    Spacer(modifier = Modifier.height(3.dp))
    Image(
        painter = rememberImagePainter(imageUrl),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clip(MaterialTheme.shapes.medium)
    )

    // Add some space between the photo and the text information
    Spacer(modifier = Modifier.height(3.dp))
    Row(){
        Text(
            text = "Animal:",
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
        Text(
            text = " ${photo.animalType}",
            fontSize = 10.sp
        )
    }
    Spacer(modifier = Modifier.height(3.dp))
    Text(
        text = "Description: ",
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp
    )
    Spacer(modifier = Modifier.height(3.dp))
    // Display the description
    Text(
        text = "${photo.description}",
        fontSize = 10.sp
    )
}
