package com.example.snappet

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import com.google.firebase.appcheck.internal.util.Logger
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun AppContent(navController: NavHostController) {
    lateinit var currentImagePath: String
    //var uri: Uri
    var takenPicture by remember { mutableStateOf<Bitmap?>(null) }

    //  val imageBitmap = remember(takenPicture) { takenPicture!!.asImageBitmap() }
    val context = LocalContext.current
    val authority = "com.example.snappet.provider"

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
    val getCameraImage =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d(Logger.TAG,"currentImagePath? : $currentImagePath")
                takenPicture = BitmapFactory.decodeFile(currentImagePath)
                Log.d(Logger.TAG,"Is takenPicture null? : ${takenPicture == null}")
            }
        }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            getCameraImage.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)

        onDispose {
            // Clean up if needed
        }

    }

    if (takenPicture != null) {
        Log.d(Logger.TAG, "not null")
        val imageBitmap = rememberUpdatedState(takenPicture!!).value.asImageBitmap()
        Log.d(Logger.TAG,"URI é: $uri")
        /*Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Navigation(navController =navController)
            }) { paddingValues ->
            Text(text = "BACANO!!!!!!!", modifier = Modifier.padding(paddingValues), color = androidx.compose.ui.graphics.Color.White)
        }*/
        //Text(text = " MUITO      BACANO!!!!!!!", color = androidx.compose.ui.graphics.Color.White)
        PhotoForm(uri = uri, imageBitmap = imageBitmap)
        //navController.navigate(Screens.Trophies.route)
        //navController.navigate(Screens.Trophies.route)
        //PhotoForm(navController = navController, uri = uri,  imageBitmap = imageBitmap)
        //SnapPetPreviewPhoto(navController = navController, uri = Uri.EMPTY , imageBitmap = imageBitmap)
    } else {
        Log.d(Logger.TAG,"Image is null")
    }
}




