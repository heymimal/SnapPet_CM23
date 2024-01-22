package com.example.snappet.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.snappet.R
import com.example.snappet.data.Photo
import com.example.snappet.navigation.Screens
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState



//meter nome do utilizador
@Composable
fun PhotoDetailScreen(photo: Photo, navController: NavController, check: Boolean) {
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

        val likePhotoMessage = remember{ mutableStateOf(false) }

        if(likePhotoMessage.value){
            AlertDialog(
                onDismissRequest = { likePhotoMessage.value = false },
                title = {Text(text = "Thank you!")},
                text = {Text (
                    text = "You like the photo!",
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )},
                confirmButton = {
                    Button(
                        onClick = {likePhotoMessage.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ){
                        Text(text = "OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        val alreadyLikedPhotoMessage = remember{ mutableStateOf(false) }

        if(alreadyLikedPhotoMessage.value){
            AlertDialog(
                onDismissRequest = { alreadyLikedPhotoMessage.value = false },
                title = {Text(text = "Warning!")},
                text = {Text (
                    text = "You have already liked the photo, can't like it again!",
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )},
                confirmButton = {
                    Button(
                        onClick = {alreadyLikedPhotoMessage.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ){
                        Text(text = "OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        var isLikeEnabled by remember { mutableStateOf(!check) }

        Log.d(TAG, "Check -> " + check)
        Log.d(TAG, "isLikeEnabled -> " + isLikeEnabled)

        Image(
            painter = painterResource(R.drawable.heart),
            contentDescription = "Like",
            modifier = Modifier
                .size(25.dp)
                .clickable(enabled = isLikeEnabled) {
                        likePhotoMessage.value = true
                        photo.likes +=1
                        updateUserLikes(photo, photo.id, onFailure = { exception ->
                            Log.e(TAG, "failed to update liked photos", exception)
                        })
                        isLikeEnabled = false
                }
            //.align(alignment = Alignment.BottomEnd)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Number of Likes: ${photo.likes}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        if(photo.longitude != 190.0){
            val positionL = LatLng(photo.latitude,photo.longitude)
            var cameraPositionState = rememberCameraPositionState{
                position = CameraPosition.fromLatLngZoom(positionL,10f)
            }
            CameraPositionState(CameraPosition.fromLatLngZoom(positionL,10f))

            val positionState = MarkerState(position = positionL)

            var uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false,
                tiltGesturesEnabled = false,
                mapToolbarEnabled = false,
            )
            GoogleMap (
                cameraPositionState = CameraPositionState(CameraPosition.fromLatLngZoom(positionL,14f)),
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp)
            ) {
                Marker(
                    state = positionState,
                    title = photo.description
                )
            }

        }

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


fun updatePhotoLikes(photo: Photo, photoId: String, newLikes: Int){
    val database = Firebase.database
    val reference = database.reference.child("imagesTest").child("allImages")
        .child(photoId).child("likes")

    reference.setValue(newLikes).addOnSuccessListener {
        Log.d(TAG, "LIKES UPDATED")
    }
        .addOnFailureListener{
            Log.d(TAG, "LIKES NOT UPDATED")
        }
}

fun updateUserLikes(
    photo: Photo,
    photoId: String,
    onFailure: (Exception) -> Unit
){
    val user = Firebase.auth.currentUser

    if (user != null) {
        val database = Firebase.database
        val reference = database.reference.child("Users (Quim)").child(user.uid).child("likedPhotos")

        // photo ID already in folder?
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentLikedPhotos = snapshot.children.mapNotNull { it.value as? String }

                for (c in currentLikedPhotos){
                    Log.d(TAG, "photo -> " + c)
                }

                // If not, add it
                if (!currentLikedPhotos.contains(photoId)) {

                    val likedPhotoReference = reference.push()
                    likedPhotoReference.setValue(photoId)
                        .addOnSuccessListener {
                            Log.d(TAG, "Liked photo added to user's liked photos list")
                        }
                        .addOnFailureListener {
                            Log.e(TAG, "Failed to add liked photo to user's liked photos list", it)
                            onFailure(it)
                        }

                        updatePhotoLikes(photo, photo.id, photo.likes)


                } else {

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

