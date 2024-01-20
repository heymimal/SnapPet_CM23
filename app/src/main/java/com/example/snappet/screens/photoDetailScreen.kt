package com.example.snappet.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.snappet.data.Photo

@Composable
fun PhotoDetailScreen(photo: Photo) {
    // Use a Column to arrange the information vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the photo with a fixed height
        Image(
            painter = rememberImagePainter(photo.imageUri.toString()),
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
    }
}
