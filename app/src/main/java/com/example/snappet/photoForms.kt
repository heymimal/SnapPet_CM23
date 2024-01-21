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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappet.data.Photo
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoForm(modifier: Modifier = Modifier, uri: Uri, imageBitmap: ImageBitmap, takenPicture : Bitmap, file: File, loc : LatLng?){

    var photoType by remember {
        mutableStateOf<String?>("")
    }

    var contextPhotoType by remember{
        mutableStateOf<String?>("")
    }

    var descriptionPhoto by remember{
        mutableStateOf<String?>("")
    }

    val storage = Firebase.storage
    val storageRef = storage.reference

    val context = LocalContext.current

    val user = Firebase.auth.currentUser
    val userId = user?.uid

    Log.d(TAG, "USER " + user?.displayName + " "+ userId)

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
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Animal Type")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val options = listOf("Bee", "Bird", "Butterfly", "Cat", "Chicken", "Cow", "Dog", "Duck", "Gecko", "Goat",
                "Horse", "Lizard", "Peacock", "Pig", "Rabbit", "Sheep")
            var expanded by remember { mutableStateOf(false) } //menu drop down aberto ou nao
            //var selectedOptionText by remember { mutableStateOf(options[0]) } //current selected
            var selectedOptionText by remember { mutableStateOf<String?>(null) } // current selected


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText?: "",
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
                                //photoType = selectedOptionText
                                photoType = if (selectedOptionText != null) selectedOptionText else null

                            },


                            )
                    }

                    Log.d(TAG, "ANIMAL SELECIONADO -> " + photoType)
                }
            }

            Log.d(TAG, "ANIMAL SELECIONADO1 -> " + photoType)



        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Context")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )

        radioButton { selectedOption ->
            // Atualizar a variável contextPhotoType com a opção selecionada
            contextPhotoType = selectedOption
        }

        Log.d(TAG, "TESTE DO CONTEXTO");
        Log.d(TAG, contextPhotoType!!);

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Description")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(15.dp))

        var ttext by remember { mutableStateOf<String?>("") }

        val maxChar = 60

        /*TextField(
            value = ttext!!,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onValueChange = {
                if(it.length <= maxChar){
                    ttext = it
                } },
            label = { Text("Description") }
        )

        descriptionPhoto = ttext*/

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // Character count display
            Text(
                text = "Characters: ${ttext?.length}/$maxChar",
                color = Color.Gray,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)

            )

            // TextField with character limit
            TextField(
                value = ttext!!,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onValueChange = {
                    if (it.length <= maxChar) {
                        ttext = it
                    }
                },
                label = { Text("Description") }
            )



            descriptionPhoto = ttext
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "(Options selected with * are mandatory to fill in!)",
            color = Color.Black,
            style = TextStyle(fontSize = 10.sp, fontStyle = FontStyle.Italic),
            modifier = Modifier.align(alignment = Alignment.Start)
        )


    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        val showAlertMessage = remember{ mutableStateOf(false) }

        if(showAlertMessage.value){
            AlertDialog(
                onDismissRequest = { showAlertMessage.value = false },
                title = {Text(text = "Warning")},
                text = {Text (
                    text = "Fill everything!",
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )},
                confirmButton = {
                    Button(
                        onClick = {showAlertMessage.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ){
                        Text(text = "OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }


        Button(
            onClick = {
                val fileName = "photo_${System.currentTimeMillis()}.jpg"

                if(photoType == "" || contextPhotoType == "" || descriptionPhoto == ""){
                        showAlertMessage.value = true
                }else{
                    showAlertMessage.value = false
                    var savedUri = saveImageToMediaStore(takenPicture,context,file)
                    //val downloadUrl = uploadImageToStorage(fileName, imageBitmap)
                    //var photo = Photo(savedUri!!, photoType!!, contextPhotoType!!, descriptionPhoto!!, "", userId.toString()!!)
                //var savedUri = saveImageToMediaStore(takenPicture,context,file)
                var latitude = 190.0
                var longitude = 190.0
                if(loc!=null){
                    latitude = loc.latitude
                    longitude = loc.longitude
                }
                var photo = Photo(savedUri!!, photoType!!, contextPhotoType!!, descriptionPhoto!!, "", "",userId!!,latitude,longitude)

                    uploadImageToStorage(fileName, imageBitmap, photo);
                }


                //uploadPhotoToDatabase(photo)
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(Color.Black),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 246.dp,
                    y = 740.dp
                )
                .height(50.dp)
                .width(150.dp)

        )
        {
            Text(text = "Upload", style = TextStyle(fontSize = 20.sp), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

}



private fun uploadImageToStorage(fileName: String, imageBitmap: ImageBitmap, photo:Photo){
    val storage = Firebase.storage
    val storageRef: StorageReference = storage.reference.child(fileName)

    val currentUser = Firebase.auth.currentUser
    val userUid = currentUser?.uid

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = database.reference.child("urlTest")

    var downloadUrl = ""

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
            var downloadUrlFirebase = ""
            val downloadUrlReal = taskSnapshot.storage.downloadUrl;
            downloadUrlReal.addOnSuccessListener { uri ->
                run {
                    var imageUrl = uri.toString()
                    downloadUrlFirebase = imageUrl
                    uploadPhotoToDatabase(photo, downloadUrlFirebase)
                }
            }
            //val downloadUrlr = taskSnapshot.metadata?.reference?.downloadUrl
            //downloadUrl = downloadUrlr.toString()

            //Log.d(TAG, "downloadurl!! -> " + downloadUrlReal)
            //Log.d(TAG, "downloadurl -> " + downloadUrl)555
            // You can use the downloadUrl for further processing or store it in your database
        }.addOnFailureListener { exception ->
            // Handle the failure case, e.g., show an error message
            Log.e(TAG, "Error uploading image to Firebase Storage: ${exception.message}")
        }
    }


}

/*private fun uploadPhotoToDatabase(photo: Photo, downloadUrl : String) {
    val currentUser = Firebase.auth.currentUser
    val database = Firebase.database
    val databaseReference = database.reference

    currentUser?.let {
        val userId = it.uid
        val userName = it.displayName

        // Create a folder name based on the user's ID or display name
        val folderName = if (!userName.isNullOrBlank()) userName else userId

        val allFolder = "allImages"

        // Update the path where the image URL will be stored in the database
        val imagePath = "imagesTest/$folderName/"

        val allImagesPath = "imagesTest/$allFolder/"

        // Reference to the database path
        val databasePath = databaseReference.child(imagePath)

        val allImagesDatabasePath = databaseReference.child(allImagesPath)

        // Push the photo data to the database
        val databaseKey = databasePath.push().key
        //photo.id = databaseKey.toString()
        Log.d(TAG, "SOME TEST " + databaseKey.toString());
        databaseKey?.let { key ->
            photo.id = key
            val data = hashMapOf(
                "imageUrl" to photo.imageUri.toString(),
                "animal" to photo.animalType,
                "context" to photo.contextPhoto,
                "description" to photo.description,
                "id" to photo.id,
                "downloadUrl" to downloadUrl,
                "sender" to it.uid
                "id" to photo.id,
                "latitude" to photo.latitude,
                "longitude" to photo.longitude
            )
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

        val allImagesdatabaseKey = allImagesDatabasePath.push().key
        allImagesdatabaseKey?.let { key ->
            photo.id = key
            val data = hashMapOf(
                "imageUrl" to photo.imageUri.toString(),
                "animal" to photo.animalType,
                "context" to photo.contextPhoto,
                "description" to photo.description,
                "id" to photo.id,
                "latitude" to photo.latitude,
                "longitude" to photo.longitude
                "id" to photo.id,
                "downloadUrl" to downloadUrl,
                "sender" to it.uid
            )
            allImagesDatabasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Photo data uploaded to Realtime Database to all images folder.")
                    } else {
                        Log.e(TAG, "Failed to upload photo data to Realtime Database", task.exception)
                    }
                }
        }
    }
}*/

//ja mete as fotos com o mesmo id em folders diferentes
private fun uploadPhotoToDatabase(photo: Photo, downloadUrl: String) {
    val currentUser = Firebase.auth.currentUser
    val database = Firebase.database
    val databaseReference = database.reference

    currentUser?.let {
        val userId = it.uid
        val userName = it.displayName

        val folderName = if (!userName.isNullOrBlank()) userName else userId
        val allFolder = "allImages"
        val imagePath = "imagesTest/$folderName/"
        val allImagesPath = "imagesTest/$allFolder/"

        val databasePath = databaseReference.child(imagePath)
        val allImagesDatabasePath = databaseReference.child(allImagesPath)

        val sharedKey = databasePath.push().key
        sharedKey?.let { key ->
            photo.id = key
            val data = hashMapOf(
                "imageUrl" to photo.imageUri.toString(),
                "animal" to photo.animalType,
                "context" to photo.contextPhoto,
                "description" to photo.description,
                "id" to photo.id,
                "downloadUrl" to downloadUrl,
                "sender" to it.uid,
                "latitude" to photo.latitude,
                "longitude" to photo.longitude
            )

            // each user folder
            databasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Photo data uploaded to user-specific folder.")
                    } else {
                        Log.e(TAG, "Failed to upload photo data to user-specific folder", task.exception)
                    }
                }

            // all images folder
            allImagesDatabasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Photo data uploaded to shared folder.")
                    } else {
                        Log.e(TAG, "Failed to upload photo data to shared folder", task.exception)
                    }
                }
        }
    }
}




private fun saveImageToMediaStore(bitmap: Bitmap, context: Context, file: File): Uri? {
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
            Log.d(TAG, "VAMOS TESTAR");
            Log.d(TAG, imageUri.toString())
        }

        return imageUri
    }

    return null

}

@Composable
fun radioButton(onOptionSelected: (String) -> Unit) {
    val radioOptions = listOf("Entertainment", "Needs Help")
    //var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    var selectedOption by remember { mutableStateOf<String?>("") }

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
                        //selectedOption = option
                        selectedOption = if (selectedOption == option) null else option
                        onOptionSelected(selectedOption!!)
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
