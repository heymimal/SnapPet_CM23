package com.example.snappet.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.snappet.R
import com.example.snappet.data.Photo
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

@Composable
fun ClusterViewPhotos(clusterPhotos : List<Photo>, reference: DatabaseReference){
    LazyColumn(
        modifier = Modifier
    ) {
       item {
            Column {
                LazyRow(
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    clusterPhotos.forEach { photo ->
                        item {
                            PhotoDetailCard(photo, reference)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailCard(photo: Photo, reference: DatabaseReference) {

    val likePhotoMessage = remember{ mutableStateOf(false) }

    if(likePhotoMessage.value){
        AlertDialog(
            onDismissRequest = { likePhotoMessage.value = false },
            title = {Text(text = "Thank you!")},
            text = {Text (
                text = "You liked the photo!",
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

    var expanded by remember { mutableStateOf(false) }
    val imageUrl = if (photo.sender != Firebase.auth.currentUser?.uid) {
        photo.downloadUrl
    } else {
        photo.imageUri.toString()
    }
    val configuration = LocalConfiguration.current
    val smallAmp = 1
    val largeAmp = 2
    val cardAmp = 3
    val smallWidth = 120.dp
    val smallHeight = 150.dp
    val largeWidth = configuration.screenWidthDp.dp
    val largeHeight = 450.dp

    var receivedValue by remember {
        mutableStateOf(false)
    }

    var isLiked by remember {
        mutableStateOf(false)
    }

    isAlreadyInFolder(reference, photo) { isAlreadyLiked ->
        Log.d(ContentValues.TAG, "Is photo already liked: $isAlreadyLiked")

        isLiked =isAlreadyLiked
        receivedValue = true
    }

    var width by remember {
        mutableStateOf(smallWidth)
    }
    var height by remember {
        mutableStateOf(smallHeight)
    }
    var amp by remember {
        mutableIntStateOf(smallAmp)
    }
    var cardAmpRemember by remember {
        mutableIntStateOf(smallAmp)
    }

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = width, height = height)
            .padding(start = 4.dp, end = 4.dp),
        onClick = {
            if(expanded){
                width = smallWidth
                height = smallHeight
                amp = smallAmp
                cardAmpRemember = smallAmp
            } else {
                width = largeWidth
                height = largeHeight
                amp = largeAmp
                cardAmpRemember = cardAmp
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
                .height((75 * amp).dp)
                .clip(MaterialTheme.shapes.medium)
        )

        // Add some space between the photo and the text information
        Spacer(modifier = Modifier.height(3.dp))
        Row(){
            Text(
                text = "Animal:",
                fontWeight = FontWeight.Bold,
                fontSize = (10*amp).sp,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = " ${photo.animalType}",
                fontSize = (10*amp).sp
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = "Uploaded by: ",
            fontWeight = FontWeight.Bold,
            fontSize = (10*amp).sp,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "${photo.senderName}",
            fontSize = (10*amp).sp,
            modifier = Modifier.padding(start = 4.dp)
        )

        if(expanded && receivedValue){
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Description: ",
                fontWeight = FontWeight.Bold,
                fontSize = (10*amp).sp,
                modifier = Modifier.padding(start = 4.dp)

            )
            Spacer(modifier = Modifier.height(3.dp))
            // Display the description
            Text(
                text = "${photo.description}",
                fontSize = (10*amp).sp,
                modifier = Modifier.padding(start = 4.dp)
            )
            var isLikeEnabled by remember { mutableStateOf(!isLiked) }

            OutlinedButton(
                enabled = isLikeEnabled,
                onClick = {
                    likePhotoMessage.value = true
                    photo.likes +=1
                    updateUserLikes(photo, photo.id, onFailure = { exception ->
                        Log.e(ContentValues.TAG, "failed to update liked photos", exception)
                    })
                    isLikeEnabled = false
                },
                content = {
                Image(painterResource(R.drawable.heart),
                    modifier = Modifier.size(40.dp),
                    contentDescription = null) // Adjust spacing
                //Text("Like!", fontSize = 15.sp)
            }
            )
        }
    }
}
fun isAlreadyInFolder(
    reference: DatabaseReference,
    photo: Photo,
    callback: (Boolean) -> Unit
) {
    reference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val currentLikedPhotos = snapshot.children.mapNotNull { it.value as? String }

            val isPhotoAlreadyLiked = currentLikedPhotos.contains(photo.id)
            callback(isPhotoAlreadyLiked)

        }

        override fun onCancelled(error: DatabaseError) {
            // Handle the onCancelled case appropriately
            callback(false)
        }
    })
}