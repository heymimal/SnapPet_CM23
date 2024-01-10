package com.example.snappet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappet.navigation.Screens
import com.example.snappet.profile.EditProfileScreen2_nav
import com.example.snappet.screens.Trophies_nav
import com.example.snappet.screens.menuBottomNav
import com.example.snappet.sign_In.GoogleAuthUiClient
import com.example.snappet.sign_In.LoginScreen2
import com.example.snappet.sign_In.SignInViewModel
import com.example.snappet.ui.theme.SnapPetTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    //criamos aqui o nosso cliente aqui
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContent {
            SnapPetTheme {
                //obtemos uma referencia do nosso NavController
                val navController = rememberNavController()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //menuBottomNav()
                    //NavGraph(navController)

                    //vai hospedar todos os nossos ecras diferentes
                    //recebe o navController e o "sign_in" routh
                    //tal como no navGraph que o Ricardo fez
                    NavHost(navController = navController, startDestination = "sign_in" ){
                        //este é o composable mostrado no sign in screen
                        //temos que dar a routh ao composable
                        composable("sign_in"){
                            //referência do nosso viewModel
                            //passamos o tipo do nosso viewModel que é SignInViewModel
                            val viewModel = viewModel<SignInViewModel>()
                            //queremos uma referencia do nosso state que podemos buscar
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            //agora o nosso "state" recebe os updates

                            //verifica se o user está loged in already
                            //se estiver vamos diretamente para o Home screen
                            LaunchedEffect(key1 = Unit){
                                //se houver um user signed in então o isto não devolve null
                                if(googleAuthUiClient.getSignedInUser() != null){

                                    //navegamos para o nosso profileScreen
                                    //navController.navigate("profile")
                                    navController.navigate(route = Screens.Home.route)
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                //vamos receber uma activityResult, neste caso vamos dar-lhe o nome de "result"
                                onResult = {result->
                                    //se recebermos um result bem sucedido
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
                                    //depois de termos conseguido fazer login com sucesso
                                    //queremos navegar para o Home Screen
                                    //e claro temos que passar a routh para onde queremos navegar


                                    //navController.navigate("profile")
                                    navController.navigate(route = Screens.Home.route)

                                    //queremos fazer reset do state do view model
                                    //para depois começarmos com um state novo (o user tem que fazer
                                    // login outra vez)
                                    viewModel.resetState()
                                }
                            }

                            //SignInScreen(
                            LoginScreen2(
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

                        //agora é para o profile
                        composable("profile"){
                            //vamos para o ecrã do Profile
                            //um dos parametros de entrada deste ecrã é o UserData, portanto
                            //vamos busca-lo ao googleAUthUiClient
                            //dantes tinha "ProfileScreen"
                            EditProfileScreen2_nav(navController,
                                userData = googleAuthUiClient.getSignedInUser(),
                                //o segundo parametro de entrada do ecrã do profile eh
                                //enviarmos a função que faz logOut
                                onSignOut = {
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
                            )

                        }
                        composable(route = Screens.Home.route) {
                            menuBottomNav(navController)
                        }
                        composable("map_route") {
                        }
                        composable("camera_route") {

                        }
                        composable(route = Screens.Trophies.route) {
                            Trophies_nav(navController)
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