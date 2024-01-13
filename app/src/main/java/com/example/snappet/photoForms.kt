package com.example.snappet

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappet.data.Photo
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


//@OptIn(ExperimentalMaterial3Api::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoForm(modifier: Modifier = Modifier, uri: Uri, imageBitmap: ImageBitmap, takenPicture : Bitmap, file: File){

    var photo = Photo(uri);

    var photoType by remember {
        mutableStateOf<String?>("n")
    }

    var contextPhotoType by remember{
        mutableStateOf<String?>("Entertainment")
    }

    var descriptionPhoto by remember{
        mutableStateOf<String?>("Dog")
    }

    val storage = Firebase.storage
    val storageRef = storage.reference

    val context = LocalContext.current


    //var navController = rememberNavController()

    Log.d(TAG, "URI");
    Log.d(TAG, uri.toString());

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
        ){
            Image(
                bitmap = imageBitmap,
                contentDescription = "description",
                modifier = Modifier
                    //.padding(16.dp, 8.dp)
                    //.size(200.dp).align(Alignment.TopCenter)
                    //.align(Alignment.CenterHorizontally)
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
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
                                photoType = selectedOptionText

                                Log.d(TAG, "TESTEEEEEEEEEEE")
                                Log.d(TAG, selectedOptionText)
                                Log.d(TAG, photoType!!)
                            },


                            )
                    }
                }
            }
            Log.d(TAG, "TESTE")
            Log.d(TAG, selectedOptionText)
            Log.d(TAG, photoType!!)


        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Context",
            color = Color.Black,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )

        radioButton { selectedOption ->
            // Atualizar a variável contextPhotoType com a opção selecionada
            contextPhotoType = selectedOption
        }

        Log.d(TAG, "TESTE DO CONTEXTO");
        Log.d(TAG, contextPhotoType!!);

        Text(
            text = "Description",
            color = Color.Black,
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(15.dp))

        var ttext by remember { mutableStateOf("Describe the Photo...") }

        TextField(
            value = ttext,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onValueChange = { ttext = it },
            label = { Text("Description") }
        )

        descriptionPhoto = ttext

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = {
                val fileName = "photo_${System.currentTimeMillis()}.jpg"
                var photo = Photo(uri, photoType!!, contextPhotoType!!, descriptionPhoto!!)

                Log.d(TAG, "TIPOS DA FOTOS SEI LA")
                Log.d(TAG, photo.imageUri.toString())
                Log.d(TAG, photo.animalType)

                Log.d(TAG, "CONTEXTO DAS FOTOS SEI LA")
                Log.d(TAG, photo.imageUri.toString())
                Log.d(TAG, photo.contextPhoto)

                Log.d(TAG, "DESCRICAO DAS FOTOS SEI LA")
                Log.d(TAG, photo.imageUri.toString())
                Log.d(TAG, photo.description)


                saveImageToMediaStore(takenPicture,context,file)
                uploadImageToStorage(fileName, imageBitmap);
                uploadPhotoToDatabase(photo)
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
                .width(150.dp)

        )
        {
            Text(text = "Upload", style = TextStyle(fontSize = 20.sp))
        }
    }

}

// Function to upload the image to Firebase Storage
private fun uploadImageToStorage(fileName: String, imageBitmap: ImageBitmap) {
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

private fun uploadPhotoToDatabase(photo: Photo) {
    val currentUser = Firebase.auth.currentUser
    val database = Firebase.database
    val databaseReference = database.reference

    currentUser?.let {
        val userId = it.uid
        val userName = it.displayName

        // Create a folder name based on the user's ID or display name
        val folderName = if (!userName.isNullOrBlank()) userName else userId

        // Update the path where the photo data will be stored in the database
        //val photoPath = "photoData/$folderName/"

        // Update the path where the image URL will be stored in the database
        val imagePath = "imagesTest/$folderName/"

        val data = hashMapOf(
            "imageUrl" to photo.imageUri.toString(),
            "animal" to photo.animalType,
            "context" to photo.contextPhoto,
            "description" to photo.description
        )

        // Reference to the database path
        val databasePath = databaseReference.child(imagePath)

        // Push the photo data to the database
        val databaseKey = databasePath.push().key
        databaseKey?.let { key ->
            //databasePath.child(key).setValue(photo)
            databasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Photo data uploaded to Realtime Database.")
                    } else {
                        Log.e(TAG, "Failed to upload photo data to Realtime Database", task.exception)
                    }
                }
        }
    }
}



private fun saveImageToMediaStore(bitmap: Bitmap, context: Context, file: File) {
    val folderName = "Snappet"

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "${file.name}")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName)
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let { imageUri ->
        contentResolver.openOutputStream(imageUri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            Toast.makeText(context, "Image saved to $folderName folder", Toast.LENGTH_SHORT).show()
        }
    }
}


/*@Composable
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
}*/

@Composable
fun radioButton(onOptionSelected: (String) -> Unit) {
    val radioOptions = listOf("Entertainment", "Needs Help")
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radioOptions.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                        onOptionSelected(selectedOption)
                    }
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
