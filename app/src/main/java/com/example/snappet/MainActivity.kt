package com.example.snappet

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.snappet.navigation.Screens
import com.example.snappet.profile.ProfileScreen
import com.example.snappet.screens.HomeMenu
import com.example.snappet.screens.TrophiesNav
import com.example.snappet.sign_In.GoogleAuthUiClient
import com.example.snappet.sign_In.LoginScreen
import com.example.snappet.sign_In.SignInViewModel
import com.example.snappet.ui.theme.SnapPetTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.appcheck.internal.util.Logger.TAG
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy { //criamos aqui o nosso cliente aqui
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private fun onSignout(navController: NavHostController){
        lifecycleScope.launch {
            googleAuthUiClient.singOut()
            Toast.makeText(
                applicationContext,
                "Signed out",
                Toast.LENGTH_LONG
            ).show()

            //faz voltarmos para o ecrã anterior (do login)
            //VER ISTO!!!
            //navController.popBackStack()
            navController.navigate("sign_in")
        }
    }

    @Composable
    private fun SignIn(navController : NavHostController){
        val viewModel = viewModel<SignInViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()//referencia do nosso state que podemos buscar
        //agora o nosso "state" recebe os updates

        LaunchedEffect(key1 = Unit){ //verifica se o user está loged in already
            if(googleAuthUiClient.getSignedInUser() != null){ //user is valid and exists
                navController.navigate(route = Screens.Home.route)
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            //vamos receber uma activityResult, neste caso vamos dar-lhe o nome de "result"
            onResult = {result->
                if(result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            //(?): caso o result não tenha sido bem sucessido podemos retornar do launch block
                            intent = result.data ?: return@launch
                        )
                        viewModel.onSignInResult(signInResult)
                    }
                }
            }
        )

        LaunchedEffect(key1 = state.isSignInSuccessful){
            if(state.isSignInSuccessful){
                Toast.makeText(
                    applicationContext,
                    "Sign in successful",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(route = Screens.Home.route)

                viewModel.resetState() //reset do state view model para dar login outra vez
            }
        }

        LoginScreen(
            state = state,
            onSignInClick ={
                lifecycleScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnapPetTheme {

                val navController = rememberNavController() // obter referencia NavController
                Log.d(TAG, "NavController: $navController")
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "sign_in" ){
                        composable("sign_in"){
                            SignIn(navController)
                        }
                        composable(Screens.Profile.route){
                            Log.d(TAG,"ge")
                            ProfileScreen(navController,
                                userData = googleAuthUiClient.getSignedInUser(), //user data stored in googleAuthClient
                                onSignOut = {onSignout(navController)}
                            )}

                        composable("Camera"){
                            AppContent(navController)


                        }
                        composable(route = Screens.Home.route) {
                            HomeMenu(navController)
                        }
                        composable(route = Screens.Trophies.route) {
                            TrophiesNav(navController)
                        }
                        /*composable(route = Screens.PhotoForm.route){
                            SnapPetPreviewPhoto(navController)
                        }*/

                        composable(route = "photo_form_screen/{capturedImageUri}") { navBackStack ->
                            // Extracting the argument
                            //val uri = Uri.parse(navBackStack.arguments?.getString("capturedImageUri") ?: "")
                            val uriString = navBackStack.arguments?.getString("capturedImageUri")?:""
                            val uri = Uri.parse(uriString);

                            //val capturedPhoto = CapturedPhoto(imageUri = uri)

                            /*if(capturedPhoto.imageUri.scheme == null){
                                Log.d(TAG, "SCHEME É NULL MANO")
                            }*/

                            //PhotoForms(modifier = Modifier, navController, uri)
                        }

                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun AppContent(navController: NavHostController) {

    lateinit var currentImagePath: String
    lateinit var strUri: String
    //var uri: Uri
    var takenPicture by remember { mutableStateOf<Bitmap?>(null) }
    var takenPictureRotated: Bitmap? = null

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
                strUri = uri.toString()
                val matrix = Matrix()
                matrix.postRotate(90F)
                takenPicture = BitmapFactory.decodeFile(currentImagePath)
                val picture = takenPicture
                // Rotate the picture taken
                takenPictureRotated = Bitmap.createBitmap(
                    picture!!,
                    0,
                    0,
                    picture.width,
                    picture.height,
                    matrix,
                    true
                )
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
        Log.d(TAG, "not null")
        val imageBitmap = remember(takenPicture) { takenPicture!!.asImageBitmap() }

        Image(
            bitmap = imageBitmap,
            contentDescription = "description",
            modifier = Modifier.padding(16.dp, 8.dp)
        )
    } else {
        Log.d(TAG,"SHIT IS null")
    }
}




