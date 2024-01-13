package com.example.snappet

import android.util.Log
import com.google.firebase.appcheck.internal.util.Logger
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

/*import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.snappet.data.Photo
import com.google.firebase.appcheck.internal.util.Logger
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class help {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoForms(modifier: Modifier = Modifier, navController: NavHostController, imageUri: Uri, imageBitmap: ImageBitmap?) {
    var photo: Photo = Photo(imageUri)

    //Log.d(TAG, "VERIFICACAO PHOTO FORMS")
    //Log.d(TAG, imageUri.path?.isNotEmpty().toString())

    val storage = Firebase.storage
    val storageRef = storage.reference

    val database = Firebase.database
    val databaseReference = database.reference

    // TODO
    // either receives the link directly or gets the latest photo available

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Photo Form",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)

        ) {
            //TODO change image to image loaded from camera
            if (imageUri == null || imageUri.scheme == null || !imageUri.scheme!!.startsWith("content")) {
                Log.e(Logger.TAG, "NULO")
                return
            }
            Log.d(Logger.TAG, "Trying to load image from Uri: $imageUri")
            /*Image(
                bitmap = imageBitmap,
                contentDescription = "Taken Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )*/
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Animal Type",
            color = Color.Black,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val options = listOf("Dog", "Cat", "Bird")
            var expanded by remember { mutableStateOf(false) } //menu drop down aberto ou nao
            var selectedOptionText by remember { mutableStateOf(options[0]) } //current selected


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = {},
                    label = { Text("Animal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                photo.type = selectedOptionText
                            },


                            )
                    }
                }
            }

            Log.d(Logger.TAG, "E AQUI??")
            Log.d(Logger.TAG, selectedOptionText)
            Log.d(Logger.TAG, photo.type)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Context",
            color = Color.Black,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )

        radioButton()

        Text(
            text = "Description",
            color = Color.Black,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(15.dp))

        var ttext by remember { mutableStateOf("Hello") }

        TextField(
            value = ttext,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onValueChange = { ttext = it },
            label = { Text("Description") }
        )
    }
    // Back Page -> Takes new photo, deletes old photo
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = { navController.navigate("camera")},
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 680.dp
                )
                .height(50.dp)
                .width(100.dp)

        )
        {
            Text(text = "Back", style = TextStyle(fontSize = 20.sp))
        }
    }

    // NEXT PAGE
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = { /*TODO*/
                Log.d(Logger.TAG, "TIPO DA FOTO")
                Log.d(Logger.TAG, photo.type);
                //uploadImageStorage(imageUri, storageRef);
                uploadImageToRealtimeDatabase(imageUri.toString(), databaseReference)
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 246.dp,
                    y = 680.dp
                )
                .height(50.dp)
                .width(100.dp)

        )
        {
            Text(text = "Next", style = TextStyle(fontSize = 20.sp))
        }
    }
}*/

private fun uploadImageToRealtimeDatabase(imageUrl: String, databaseReference: DatabaseReference) {
    val currentUser = Firebase.auth.currentUser
    currentUser?.let {
        val userId = it.uid
        val userName = it.displayName

        // Create a folder name based on the user's ID or display name
        val folderName = if (!userName.isNullOrBlank()) userName else userId

        // Update the path where the image URL will be stored in the database
        val imagePath = "images/$folderName/"

        // Construct the data to be uploaded
        val data = hashMapOf(
            "imageUrl" to imageUrl
        )

        // Reference to the database path
        val databasePath = databaseReference.child(imagePath)

        // Push the data to the database
        val databaseKey = databasePath.push().key
        databaseKey?.let { key ->
            databasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Logger.TAG, "Data uploaded to Realtime Database.")
                    } else {
                        Log.e(Logger.TAG, "Failed to upload data to Realtime Database", task.exception)
                    }
                }
        }
    }
}