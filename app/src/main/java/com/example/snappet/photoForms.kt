package com.example.snappet

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.snappet.data.Photo
import com.example.snappet.navigation.Navigation
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoForm(uri: Uri, imageBitmap: ImageBitmap){

    val storage = Firebase.storage
    val storageRef = storage.reference

    val database = Firebase.database
    val databaseReference = database.reference
    //var navController = rememberNavController()

    Log.d(TAG, "URI");
    Log.d(TAG, uri.toString());

    Column {
        Text(text = "Como")
        Text(text = "COMO CARAGO")
        Image(
            bitmap = imageBitmap,
            contentDescription = "description",
            modifier = Modifier.padding(16.dp, 8.dp)
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = {
                val fileName = "photo_${System.currentTimeMillis()}.jpg"
                uploadImageToStorage1(fileName, imageBitmap);
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

    Text(text = "Pouco     BACANO!!!!!!!", color = androidx.compose.ui.graphics.Color.White)
}

// Function to upload the image to Firebase Storage
private fun uploadImageToStorage1(fileName: String, imageBitmap: ImageBitmap) {
    val storage = Firebase.storage
    val storageRef: StorageReference = storage.reference.child(fileName)

    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    if(userUid!= null){
        val userFolderRef = storage.reference.child("user_images_storage/$userUid")

        // Create a unique filename for the image in the user's folder
        val filePath = "$fileName.jpg"
        val imageRef = userFolderRef.child(filePath)

        // Convert ImageBitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()

        // Convert ImageBitmap to Bitmap (asAndroidBitmap)
        val androidBitmap = imageBitmap.asAndroidBitmap()

        // Encode the Bitmap as JPEG
        androidBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

        val data = byteArrayOutputStream.toByteArray()

        // Upload the image to Firebase Storage
        val uploadTask: UploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image upload success, you can retrieve the download URL if needed
            val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
            // You can use the downloadUrl for further processing or store it in your database
        }.addOnFailureListener { exception ->
            // Handle the failure case, e.g., show an error message
            Log.e(TAG, "Error uploading image to Firebase Storage: ${exception.message}")
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapPetPreviewPhoto(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = " ", modifier = Modifier.padding(paddingValues))
        PhotoForms(Modifier,navController, imageUri = Uri.EMPTY, imageBitmap = null)
    }
}


@Composable
fun radioButton(){
    val radioOptions = listOf("Entertainment", "Needs Help")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment =  Alignment.CenterVertically
    ) {
        radioOptions.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { selectedOption = option }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    }
}

/*private fun uploadImageStorage(imageUri: Uri, storageRef: StorageReference){

    if (imageUri == null || imageUri.scheme == null || !imageUri.scheme!!.startsWith("content")) {
        Log.e(TAG, "Uri inválido ou nulo")
        return
    }

    Log.d(TAG, "Path do Uri: ${imageUri.path}")

    val currentUser = Firebase.auth.currentUser
    if(currentUser != null){
        val userId = currentUser.uid
        val userName = currentUser.displayName

        // Create a folder name based on the user's ID or display name
        val folderName = if (!userName.isNullOrBlank()) userName else userId

        // Update the folder path where the image will be stored
        val folderPath = "imagesTestNew/$folderName/"

        // Upload the image to Firebase Storage
        val storageFileNameUser = "$folderPath${System.currentTimeMillis()}.jpg"

        val storageReference = storageRef.child(storageFileNameUser)

        //val imageUri = capturedPhoto.imageUri

        val uploadTask1 = storageReference.putFile(imageUri)

        uploadTask1.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Photo uploaded to Firebase Storage.")
            } else {
                Log.d(TAG, "Failed to upload photo", task.exception)
            }
        }

        // Upload the image to Firebase Storage
        val storageFileName = "imagesTest/${System.currentTimeMillis()}.jpg"

        val storageReference1 = storageRef.child(storageFileName)
        val uploadTask = imageUri?.let { storageReference1.putFile(it) }

        uploadTask?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Photo uploaded to Firebase Storage.")
            } else {
                Log.e(TAG, "Failed to upload photo", task.exception)
            }
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
                        Log.d(TAG, "Data uploaded to Realtime Database.")
                    } else {
                        Log.e(TAG, "Failed to upload data to Realtime Database", task.exception)
                    }
                }
        }
    }
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
                Log.e(TAG, "NULO")
                return
            }
            Log.d(TAG, "Trying to load image from Uri: $imageUri")
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

            Log.d(TAG, "E AQUI??")
            Log.d(TAG, selectedOptionText)
            Log.d(TAG, photo.type)
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
                        Log.d(TAG, "TIPO DA FOTO")
                        Log.d(TAG, photo.type);
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
}



/*
@Preview
@Composable
private fun PhotoFormsPreview() {
    SnapPetPreviewPhoto(null)
}*/

/*
 val currentUser = Firebase.auth.currentUser

                if(currentUser != null){
                    val userId = currentUser.uid
                    val userName = currentUser.displayName

                    // Create a folder name based on the user's ID or display name
                    val folderName = if (!userName.isNullOrBlank()) userName else userId

                    // Update the folder path where the image will be stored
                    val folderPath = "images/$folderName/"

                    // Upload the image to Firebase Storage
                    val storageFileNameUser = "$folderPath${System.currentTimeMillis()}.jpg"

                    val storageReference = storageRef.child(storageFileNameUser)
                    val uploadTask1 = localUri?.let { storageReference.putFile(it) }

                    uploadTask1?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Log.d(TAG, "Photo uploaded to Firebase Storage.")
                        } else {

                            Log.e(TAG, "Failed to upload photo", task.exception)
                        }
                    }

                    // Upload the image to Firebase Storage
                    val storageFileName = "images/${System.currentTimeMillis()}.jpg"

                    val storageReference1 = storageRef.child(storageFileName)
                    val uploadTask = localUri?.let { storageReference1.putFile(it) }

                    uploadTask?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Photo uploaded to Firebase Storage.")
                        } else {
                            Log.e(TAG, "Failed to upload photo", task.exception)
                        }
                    }
                }
*/
