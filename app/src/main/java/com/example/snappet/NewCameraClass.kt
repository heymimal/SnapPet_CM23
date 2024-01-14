package com.example.snappet

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.appcheck.internal.util.Logger
import com.google.firebase.appcheck.internal.util.Logger.TAG
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects



@Composable
fun CameraClass(navController: NavController) {
    val context = LocalContext.current

    lateinit var currentImagePath: String
    lateinit var mFusedLocationClient : FusedLocationProviderClient
    var locationPoint by remember { mutableStateOf<Location?>(null)}
    var takenPicture by remember { mutableStateOf<Bitmap?>(null) }

    val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED


    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA)

    val authority = "com.example.snappet.provider"

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (storageDir != null) {
        Log.d(TAG,"${storageDir.absolutePath}")
    }
    val file = File.createTempFile(
        "Snappet_$timeStamp",
        ".jpg", /* suffix */
        storageDir   /* directory */
    ).apply { currentImagePath = absolutePath }
    var uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        authority,
        file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY);
    }
    val getCameraImage =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                capturedImageUri = uri;

                takenPicture = BitmapFactory.decodeFile(currentImagePath)

            }
        }
    val locationPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                acc && isPermissionGranted
            }
            if (!permissionsGranted) {
                //Logic when the permissions were not granted by the user
            } else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if(location != null){
                            Log.d(TAG, "Location ain't null: $location")
                            locationPoint= location
                        } else {
                            Log.d(TAG,"No location")
                        }
                    }
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                getCameraImage.launch(uri)
            }
        } )


    /*val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            getCameraImage.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }*/
    DisposableEffect(Unit) {
        //permissionLauncher.launch(Manifest.permission.CAMERA)
        locationPermission.launch(locationPermissions)
        onDispose {
            // Clean up if needed
        }

    }

    if (takenPicture != null) {
        Log.d(Logger.TAG, "not null")
        val imageBitmap = rememberUpdatedState(takenPicture!!).value.asImageBitmap()
        Log.d(Logger.TAG,"URI é: $uri")
        Log.d(TAG,locationPoint.toString())
        PhotoForm(uri = uri, imageBitmap = imageBitmap, takenPicture = takenPicture!!, file = file)
       /*Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                    bitmap = imageBitmap,
            contentDescription = "description",
            modifier = Modifier
                .padding(16.dp, 8.dp)
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
            )
            Button(onClick = { saveImageToMediaStore(takenPicture!!,context,file) },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Save")
            }
            }
        */}

}

//Text(text = " MUITO      BACANO!!!!!!!", color = androidx.compose.ui.graphics.Color.White)
//PhotoForm(uri = uri, imageBitmap = imageBitmap)
/*Column {
    Text(text = "Como")
    Text(text = "COMO CARAGO")
    Image(
        bitmap = imageBitmap,
        contentDescription = "description",
        modifier = Modifier.padding(16.dp, 8.dp)
    )
    /*Button(onClick = { navController.popBackStack() }) {
        Text(text = "Nope")
    }*/
}*/

//PhotoForm(navController = navController, uri = uri,  imageBitmap = imageBitmap)
//SnapPetPreviewPhoto(navController = navController, uri = Uri.EMPTY , imageBitmap = imageBitmap)
//* } else {
/*  Log.d(Logger.TAG,"Image is null")
}*/
// Function to save the image to a specific folder in the device's media store


