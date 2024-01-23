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
fun CardWithText(text: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

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
fun ThreeByThreeGrid1(navController: NavHostController) {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = database.reference.child("SnapPhoto").child("allImages")

    var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

    var orderedPhotosLikes by remember { mutableStateOf(emptyList<Photo>()) }

    val user = Firebase.auth.currentUser
    val userId = user?.uid

    var hasBeePhotos = recentPhotos.any {it.animalType == "Bee"}
    var hasBirdPhotos = recentPhotos.any {it.animalType == "Bird"}
    var hasButterflyPhotos = recentPhotos.any {it.animalType == "Butterfly"}
    var hasCatsPhotos = recentPhotos.any {it.animalType == "Cat"}
    var hasChickenPhotos = recentPhotos.any {it.animalType == "Chicken"}
    var hasCowPhotos = recentPhotos.any {it.animalType == "Cow"}
    var hasDogPhotos = recentPhotos.any {it.animalType == "Dog"}
    var hasDuckPhotos = recentPhotos.any {it.animalType == "Duck"}
    var hasGeckoPhotos = recentPhotos.any {it.animalType == "Gecko"}
    var hasGoatPhotos = recentPhotos.any {it.animalType == "Goat"}
    var hasHorsePhotos = recentPhotos.any {it.animalType == "Horse"}
    var hasLizardPhotos = recentPhotos.any {it.animalType == "Lizard"}
    var hasPeacockPhotos = recentPhotos.any {it.animalType == "Peacock"}
    var hasPigPhotos = recentPhotos.any {it.animalType == "Pig"}
    var hasRabbitPhotos = recentPhotos.any {it.animalType == "Rabbit"}
    var hasSheepPhotos = recentPhotos.any {it.animalType == "Sheep"}



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
                    var c = 0
                    recentPhotos.forEach { photo ->
                        if(c < 11){
                            item { CardWithImageAndText(
                                photo = photo, text = photo.animalType
                            ) {
                                navController.navigate("${Screens.PhotoDetail.route}${photo.id}")
                            }
                            }
                            c++
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
                    if (hasBeePhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Bee" }.forEach { beePhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = beePhoto,
                                    text = beePhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${beePhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No bee photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Birds", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasBirdPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Bird" }.forEach { birdPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = birdPhoto,
                                    text = birdPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${birdPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No birds photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Butterflies", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasButterflyPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Butterfly" }.forEach { butterflyPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = butterflyPhoto,
                                    text = butterflyPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${butterflyPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No butterflies photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasCatsPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Cat" }.forEach { catPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = catPhoto,
                                    text = catPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${catPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No cat photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Chickens", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasChickenPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Chicken" }.forEach { chickenPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = chickenPhoto,
                                    text = chickenPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${chickenPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No chicken photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Cows", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasCowPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Cow" }.forEach { cowPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = cowPhoto,
                                    text = cowPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${cowPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No cows photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Dogs", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasDogPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Dog" }.forEach { dogPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = dogPhoto,
                                    text = dogPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${dogPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No dogs photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Ducks", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasDuckPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Duck" }.forEach { duckPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = duckPhoto,
                                    text = duckPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${duckPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No ducks photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Geckos", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasGeckoPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Gecko" }.forEach { geckoPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = geckoPhoto,
                                    text = geckoPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${geckoPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No geckos photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Goats", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasGoatPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Goat" }.forEach { goatPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = goatPhoto,
                                    text = goatPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${goatPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No goats photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Horses", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasHorsePhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Horse" }.forEach { horsePhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = horsePhoto,
                                    text = horsePhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${horsePhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No horses photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Lizards", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasLizardPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Lizard" }.forEach { lizardPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = lizardPhoto,
                                    text = lizardPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${lizardPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No lizards photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Peacocks", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasPeacockPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Peacock" }.forEach { peacockPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = peacockPhoto,
                                    text = peacockPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${peacockPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No peacocks photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Pigs", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasPigPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Pig" }.forEach { pigPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = pigPhoto,
                                    text = pigPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${pigPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No pigs photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Rabbits", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasRabbitPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Rabbit" }.forEach { rabbitPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = rabbitPhoto,
                                    text = rabbitPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${rabbitPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No rabbits photos available.")
                        }
                    }
                }
            }
        }

        item {
            Column {
                Text("Sheeps", fontWeight = FontWeight.Bold)
                LazyRow {
                    if (hasSheepPhotos) {
                        // Display CardWithImageAndText for each bee photo
                        recentPhotos.filter { it.animalType == "Sheep" }.forEach { sheepPhoto ->
                            item {
                                CardWithImageAndText(
                                    photo = sheepPhoto,
                                    text = sheepPhoto.animalType
                                ) {
                                    navController.navigate("${Screens.PhotoDetail.route}${sheepPhoto.id}")
                                }
                            }
                        }
                    } else {
                        // Display CardWithText if there are no bee photos
                        item {
                            CardWithText(text = "No sheeps photos available.")
                        }
                    }
                }
            }
        }

    }
}


