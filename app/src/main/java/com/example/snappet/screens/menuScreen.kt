package com.example.snappet.screens

import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.snappet.R
import com.example.snappet.data.Photo
import com.example.snappet.navigation.Navigation
import com.example.snappet.navigation.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenu(navController: NavHostController) {

    val database = Firebase.database
    val databaseReference = database.reference

    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController,0)
        }) {paddingValues ->
        Text(text = "", modifier = Modifier.padding(paddingValues = paddingValues))
        ThreeByThreeGrid1(navController)

    }
}


@Composable
fun CardWithImageAndText(photo: Photo, text: String, onPhotoClick: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(8.dp)
            .clickable { onPhotoClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {


            // Use downloadUrl if sender is not the current user
            val imageUrl = if (photo.sender != Firebase.auth.currentUser?.uid) {
                photo.downloadUrl
            } else {
                photo.imageUri.toString()
            }

            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = photo.likes.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(end = 4.dp, start = 31.dp)
                )
                Image(
                    painter = painterResource(R.drawable.heart),
                    contentDescription = "Like",
                    modifier = Modifier
                        .size(25.dp)

                )


            }


        }
    }
}


@Composable
fun ThreeByThreeGrid1(navController: NavHostController) {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = database.reference.child("imagesTest").child("allImages")

    var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

    var orderedPhotosLikes by remember { mutableStateOf(emptyList<Photo>()) }

    val user = Firebase.auth.currentUser
    val userId = user?.uid

    // Retrieve recent photos from the Realtime Database
    LaunchedEffect(key1 = databaseReference) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val photos = mutableListOf<Photo>()
                for (childSnapshot in dataSnapshot.children) {
                    val imageUrl = childSnapshot.child("imageUrl").getValue(String::class.java)
                    val animalType = childSnapshot.child("animal").getValue(String::class.java)
                    val contextPhoto = childSnapshot.child("context").getValue(String::class.java)
                    val description = childSnapshot.child("description").getValue(String::class.java)
                    val id = childSnapshot.child("id").getValue(String::class.java)
                    val downloadUrl = childSnapshot.child("downloadUrl").getValue(String::class.java)
                    val sender = childSnapshot.child("sender").getValue(String::class.java)
                    val latitude = childSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude  = childSnapshot.child("longitude").getValue(Double::class.java)
                    val likes  = childSnapshot.child("likes").getValue(Int::class.java)

                    imageUrl?.let {
                        val photo = Photo(
                            imageUri = Uri.parse(it),
                            animalType = animalType ?: "",
                            contextPhoto = contextPhoto ?: "",
                            description = description ?: "",
                            id = id ?: "",
                            downloadUrl = downloadUrl ?: "",
                            sender = sender ?: "",
                            latitude = latitude ?: 0.0,
                            longitude = longitude ?: 0.0,
                            likes = likes ?: 0
                        )
                        photos.add(photo)
                    }
                }
                recentPhotos = photos.reversed()
                orderedPhotosLikes = photos.sortedByDescending{ it.likes}
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseReference.addValueEventListener(valueEventListener)


    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First Row: "Animais"

        item {
            Column {
                Text("Recent Photos", fontWeight = FontWeight.Bold)
                //Log.d(TAG, LoadRecentPhotos(databaseReference)?.size.toString())
                LazyRow {
                    recentPhotos.forEach { photo ->

                        item { CardWithImageAndText(
                            photo = photo, text = photo.animalType
                        ) {
                            navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                        }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("My Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.sender == userId){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Most Liked Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    orderedPhotosLikes.forEach { photo ->
                        item { CardWithImageAndText(
                            photo = photo, text = photo.animalType
                        ) {
                            navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                        }
                        }

                    }
                }
            }
        }

        item {
            Column {
                Text("Bees", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Bee"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Birds", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Bird"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Butterflies", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Butterfly"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Cat"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Chickens", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Chicken"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Cows", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Cow"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Dogs", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Dog"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Ducks", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Duck"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Geckos", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Gecko"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Goats", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Goat"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Horses", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Horse"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Lizards", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Lizard"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Peacocks", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Peacock"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Pigs", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Pig"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Rabbits", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Rabbit"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Sheeps", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Sheep"){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                            }
                            }
                        }
                    }
                }
            }
        }

    }
}





