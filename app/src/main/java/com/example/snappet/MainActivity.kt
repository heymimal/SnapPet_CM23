package com.example.snappet

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappet.navigation.Screens
import com.example.snappet.profile.ProfileScreen
import com.example.snappet.screens.TrophiesNav
import com.example.snappet.screens.HomeMenu
import com.example.snappet.sign_In.GoogleAuthUiClient
import com.example.snappet.sign_In.LoginScreen
import com.example.snappet.sign_In.SignInViewModel
import com.example.snappet.ui.theme.SnapPetTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.appcheck.internal.util.Logger.TAG
import kotlinx.coroutines.launch



//class SharedViewModel : ViewModel() {
    //val navController = rememberNavController()
//}

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
        val context = this
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
                            ProfileScreen(navController,
                                userData = googleAuthUiClient.getSignedInUser(), //user data stored in googleAuthClient
                                onSignOut = {onSignout(navController)}
                            )}
                        composable(route = Screens.Home.route) {
                            HomeMenu(navController)
                        }
                        composable(route = Screens.Trophies.route) {
                            TrophiesNav(navController)
                        }
                        composable(route = Screens.PhotoForm.route){
                            SnapPetPreviewPhoto(navController)
                        }
                    }
                }
            }
        }
    }
}
