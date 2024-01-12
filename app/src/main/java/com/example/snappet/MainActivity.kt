package com.example.snappet

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


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

                            PhotoForms(modifier = Modifier, navController, uri)
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

    val context = LocalContext.current
    val file = context.createImageFile()
    val authority = "com.example.snappet.provider"
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        authority, file
    )
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
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

    if (capturedImageUri.path?.isNotEmpty() == true) {
        Log.d(TAG, "VERIFICACAO")
        Log.d(TAG, capturedImageUri.path?.isNotEmpty().toString())
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = null
        )
    }

    Log.d(TAG, "TESTE DE NULO IMG URI NO MAIN ACTIVITY")
    Log.d(TAG, capturedImageUri.toString())
    
    PhotoForms(navController = navController, imageUri = capturedImageUri)

    Log.d(TAG, "TESTEEEEE")

    //navController.navigate(Screens.PhotoForm.route);
    //navController.navigate(route = "${Screens.PhotoForm.route}/${capturedImageUri}")

    Log.d(TAG, navController.toString())

    //navController.navigate("photo_form_screen/{capturedImageUri}")
}




fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}









/*Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val permissionCheckResult =
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(uri)
            } else {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text(text = "Capture Image From Camera")
        }
    }*/