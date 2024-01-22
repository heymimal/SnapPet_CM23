package com.example.snappet.database

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.example.snappet.data.Photo
import com.example.snappet.sign_In.UserData
import com.example.snappet.updateDailyMissions
import com.example.snappet.updateMonthlyMissions
import com.example.snappet.viewModels.PhotosViewModel
import com.google.firebase.appcheck.internal.util.Logger
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


fun uploadImageToStorage(fileName: String, imageBitmap: ImageBitmap, photo: Photo, userData: UserData){
    val storage = Firebase.storage
    val storageRef: StorageReference = storage.reference.child(fileName)

    var photosViewModel = PhotosViewModel()
    photosViewModel.fetchAllPhotos()

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
                    uploadPhotoToDatabase(photo, downloadUrlFirebase, userData, photosViewModel)
                }
            }
        }.addOnFailureListener { exception ->
            // Handle the failure case, e.g., show an error message
            Log.e(Logger.TAG, "Error uploading image to Firebase Storage: ${exception.message}")
        }
    }
}

//ja mete as fotos com o mesmo id em folders diferentes
fun uploadPhotoToDatabase(photo: Photo, downloadUrl: String, userData: UserData, photosViewModel: PhotosViewModel) {
    val currentUser = Firebase.auth.currentUser
    val database = Firebase.database
    val databaseReference = database.reference

    var post1 = false
    var post2 = false

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
                "longitude" to photo.longitude,
                "likes" to photo.likes
            )

            // each user folder
            databasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        updateDailyMissions(userData, photo.animalType!!)
                        updateMonthlyMissions(userData, photo.animalType!!)
                        checkForGeofence(photo, photosViewModel.photos.value)
                        Log.d(Logger.TAG, "Photo data uploaded to user-specific folder.")
                    } else {
                        Log.e(Logger.TAG, "Failed to upload photo data to user-specific folder", task.exception)
                    }
                }

            // all images folder
            allImagesDatabasePath.child(key).setValue(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Logger.TAG, "Photo data uploaded to shared folder.")
                    } else {
                        Log.e(Logger.TAG, "Failed to upload photo data to shared folder", task.exception)
                    }
                }
        }
    }
}


fun saveImageToMediaStore(bitmap: Bitmap, context: Context, file: File): Uri? {
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
            Log.d(Logger.TAG, "VAMOS TESTAR");
            Log.d(Logger.TAG, imageUri.toString())
        }

        return imageUri
    }

    return null

}


fun checkForGeofence(photo: Photo, photos : List<Photo>?){
    Log.d(TAG,"here")
    val thresholdDistance = 1500.0 // fotos a 1500 metros
    val count = isNewPhotoNearby(photo,photos!!,thresholdDistance)
    Log.d(TAG, count.toString())
    val geofenceRef = FirebaseDatabase.getInstance().getReference("geofences")
    if(count > 2) { // count would be 10, but for testing purposes
        //check if any geopoint is in the range of the photo
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var exists = false
                    for(snap in snapshot.children){
                        val geofenceKey = snap.key
                        val animalType = snap.child("animalType").getValue(String::class.java)
                        var radius = snap.child("radius").getValue(Double::class.java)
                        var latitude = snap.child("latitude").getValue(Double::class.java)
                        var longitude = snap.child("longitude").getValue(Double::class.java)

                        val isInGeoPoint = haversine(photo.latitude,photo.longitude,latitude!!,longitude!!)
                        if(animalType == photo.animalType && isInGeoPoint <= thresholdDistance){
                            Log.d(TAG,"Already exists one!")
                            exists = true
                            break
                        }
                    }
                    if(!exists){
                        Log.d(TAG,"Adding to the database")
                        val newGeofenceKey = geofenceRef.push().key
                        val newGeofenceData = hashMapOf(
                            "animalType" to photo.animalType,
                            "radius" to 1500.0,
                            "latitude" to photo.latitude,
                            "longitude" to photo.longitude,
                        )
                        if (newGeofenceKey != null) {
                            geofenceRef.child(newGeofenceKey).setValue(newGeofenceData)
                            Log.d(TAG, "Added a new geofence to the database")
                        }
                    }
                } else {
                    Log.d(TAG,"Adding to the database")
                    val newGeofenceKey = geofenceRef.push().key
                    val newGeofenceData = hashMapOf(
                        "animalType" to photo.animalType,
                        "radius" to 1500.0,
                        "latitude" to photo.latitude,
                        "longitude" to photo.longitude,
                    )
                    if (newGeofenceKey != null) {
                        geofenceRef.child(newGeofenceKey).setValue(newGeofenceData)
                        Log.d(TAG, "Added a new geofence to the database")
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                println("Error: ${databaseError.message}")
            }
        }
        geofenceRef.addListenerForSingleValueEvent(valueEventListener)
    }

}

fun isNewPhotoNearby(newPhoto: Photo, existingPhotos: List<Photo>, thresholdDistance: Double) : Int{
    var c = 0
    Log.d(TAG,"My photo is in latitude: ${newPhoto.latitude} and on longitude: ${newPhoto.longitude}")
    for (photo in existingPhotos) {
        if(photo.latitude != 190.0 && photo.id != newPhoto.id && photo.animalType == newPhoto.animalType){
            val distance = haversine(newPhoto.latitude,newPhoto.longitude,photo.latitude,photo.longitude)
            if (distance <= thresholdDistance) {
                c++;
            }
        }
    }
    Log.d(TAG,"Photos nearby: $c")
    return c
}

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371000 // Earth radius in meters

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

    return R * c
}

@Composable
fun getPhotos(databaseReference: DatabaseReference): List<Photo>{

    var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

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
                            downloadUrl = downloadUrl?: "",
                            sender = sender?: "",
                            latitude = latitude ?: 0.0,
                            longitude = longitude ?: 0.0,
                            likes = likes ?: 0
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

    return recentPhotos
}