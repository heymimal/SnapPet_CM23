package com.example.snappet.screens

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenu(navController: NavHostController) {

    val database = Firebase.database
    val databaseReference = database.reference

    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    //var recentPhotos by remember { mutableStateOf<List<Photo>>(emptyList()) }
    //var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

    /*LaunchedEffect(Unit) {
        LoadRecentPhotos(databaseReference)?.let {
            recentPhotos = it
        }
    }*/

    /*LaunchedEffect(Unit) {
        coroutineScope {
            LoadRecentPhotos(databaseReference)?.let {
                recentPhotos = it
            }
        }
    }*/



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "", modifier = Modifier.padding(paddingValues = paddingValues))
        ThreeByThreeGrid1(navController)

    }
}


@Composable
fun CardWithImageAndText(photo: Photo, imageUrl: String, text: String, onPhotoClick: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .padding(8.dp)
            .clickable { onPhotoClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            /*Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )*/
            Log.d("ImageURL", photo.imageUri.toString())

            Image(
                painter = rememberImagePainter(imageUrl),
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
fun ThreeByThreeGrid1(navController: NavHostController) {

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = database.reference.child("imagesTest").child("allImages")

    var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

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

                    //val key = childSnapshot.key;
                    //Log.d(TAG, "NOVO TESTE! " + key);

                    //Log.d(TAG, "Image URLLL: $imageUrl")

                    imageUrl?.let {
                        val photo = Photo(
                            imageUri = Uri.parse(it),
                            animalType = animalType ?: "",
                            contextPhoto = contextPhoto ?: "",
                            description = description ?: "",
                            id = id ?: "",
                        )
                        photos.add(photo)
                    }
                }
                recentPhotos = photos
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
       /* item {
            Column {
                Text("Animais", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock") }
                }
            }
        }*/


        // Second Row: "Most Recent Photos"
        /*item {
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
                Text("Recent Photos", fontWeight = FontWeight.Bold)
                //Log.d(TAG, LoadRecentPhotos(databaseReference)?.size.toString())
                LazyRow {
                    recentPhotos.forEach { photo ->
                        Log.d(TAG, "TESTAR SEI LA ")
                        Log.d(TAG, photo.animalType)
                        Log.d(TAG, photo.contextPhoto)
                        Log.d(TAG, photo.description)
                        Log.d(TAG, photo.imageUri.toString())
                        Log.d(TAG, "Teste 1 -> : ${photo.imageUri.toString()}")
                        Log.d(TAG, "")
                        item { CardWithImageAndText(photo = photo, photo.imageUri.toString(), text = photo.animalType)}
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
                            item { CardWithImageAndText(photo = photo, photo.imageUri.toString(), text = photo.animalType)}
                        }
                    }
                }
            }
        }*/

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Cat"){
                            item { CardWithImageAndText(photo = photo, photo.imageUri.toString(),
                                text = photo.animalType, onPhotoClick = {
                                    navController.navigate("${Screens.PhotoDetail.route}${photo.id}")

                                })}
                        }
                    }
                }
            }
        }

        /*item {
            Column {
                Text("Birds", fontWeight = FontWeight.Bold)
                LazyRow {
                    recentPhotos.forEach { photo ->
                        if(photo.animalType == "Bird"){
                            item { CardWithImageAndText(photo = photo, photo.imageUri.toString(), text = photo.animalType)}
                        }
                    }
                }
            }
        }*/





    }
}


suspend fun LoadRecentPhotos(databaseReference: DatabaseReference): List<Photo>? =
    suspendCoroutine { continuation ->
        val query = databaseReference.child("imagesTest").child("allImages")
            .limitToLast(3)

        val onDataChange = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val photosList = mutableListOf<Photo>()

                dataSnapshot.children.forEach { photoSnapshot ->
                    val imageUrl = photoSnapshot.child("imageUrl").getValue(String::class.java)
                    val animalType = photoSnapshot.child("animal").getValue(String::class.java)
                    val contextPhoto = photoSnapshot.child("context").getValue(String::class.java)
                    val description = photoSnapshot.child("description").getValue(String::class.java)

                    if (imageUrl != null) {
                        val photo = Photo(
                            imageUri = Uri.parse(imageUrl),
                            animalType = animalType.orEmpty(),
                            contextPhoto = contextPhoto.orEmpty(),
                            description = description.orEmpty()
                        )



                        photosList.add(photo)


                    }
                }

                Log.d(TAG, "tamanho do photosList ")
                Log.d(TAG, photosList.size.toString())
                Log.d(TAG, photosList[0].imageUri.toString())


                continuation.resume(photosList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error loading photos from database: ${databaseError.message}")
                continuation.resume(null)
            }
        }

        query.addListenerForSingleValueEvent(onDataChange)
    }








data class ImageData(val path: String, val name: String)
