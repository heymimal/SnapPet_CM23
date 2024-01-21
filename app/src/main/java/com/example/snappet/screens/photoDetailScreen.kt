package com.example.snappet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
