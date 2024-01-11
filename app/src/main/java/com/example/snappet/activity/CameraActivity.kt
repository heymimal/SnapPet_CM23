package com.example.snappet.activity


import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappet.MainActivity
import com.example.snappet.R
import com.example.snappet.SnapPetPreviewPhoto
import com.example.snappet.databinding.ActivityCameraBinding
import com.example.snappet.navigation.Screens
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity(){
    private lateinit var viewBinding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    val storage = Firebase.storage
    val storageRef = storage.reference

    val database = Firebase.database
    val databaseReference = database.reference

    private lateinit var navController : NavHostController

    private lateinit var composeView: ComposeView

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        composeView = findViewById(R.id.composeView)

        //Log.d(TAG,composeView.toString())


        //Log.d(TAG,"BEFORE composeView")

        composeView.setContent {
            navController = rememberNavController()
            //activateNav(navController = navController)
        }
        Log.d(TAG,"INSIDE composeView")
        //navController =
        //Log.d(TAG,navController.toString())


        //Log.d(TAG,"AFTER composeView")

        //Log.d(TAG,navController.toString())

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.videoCaptureButton.setOnClickListener { loadPhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()


    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SnapPet")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()


        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){

                    //val msg = "Photo capture succeeded: ${output.savedUri}"
                    //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    //Log.d(TAG, msg)

                    //Log.d(TAG,navController.toString())
                    navController.navigate(Screens.PhotoForm.route)
                            //Log.d(TAG,navController.visibleEntries.toString())



                    //val localUri = output.savedUri

                    //if (localUri != null) {
                        //uploadImageStorage(localUri)

                        //uploadImageToRealtimeDatabase(localUri.toString())


                        // change
                    //}
                }
            }
        )
    }

    private fun uploadImageToRealtimeDatabase(imageUrl: String) {
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
                            Toast.makeText(
                                baseContext,
                                "Data uploaded to Realtime Database",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d(TAG, "Data uploaded to Realtime Database.")
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Failed to upload data to Realtime Database",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(TAG, "Failed to upload data to Realtime Database", task.exception)
                        }
                    }
            }
        }
    }


    private fun uploadImageStorage(localUri: Uri){

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
                    Toast.makeText(baseContext, "Photo uploaded to Firebase Storage", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Photo uploaded to Firebase Storage.")
                } else {
                    Toast.makeText(baseContext, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to upload photo", task.exception)
                }
            }

            // Upload the image to Firebase Storage
            val storageFileName = "images/${System.currentTimeMillis()}.jpg"

            val storageReference1 = storageRef.child(storageFileName)
            val uploadTask = localUri?.let { storageReference1.putFile(it) }

            uploadTask?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Photo uploaded to Firebase Storage", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Photo uploaded to Firebase Storage.")
                } else {
                    Toast.makeText(baseContext, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Failed to upload photo", task.exception)
                }
            }
        }
    }

    private fun loadPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"

        // Start the image picker activity
        startActivityForResult(intent, PICK_IMAGES_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the selected images
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                uploadImageStorage(it)

                uploadImageToRealtimeDatabase(it.toString())
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PICK_IMAGES_REQUEST_CODE = 123
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}