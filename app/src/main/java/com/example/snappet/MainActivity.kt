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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snappet.navigation.Screens
import com.example.snappet.profile.EditProfileScreen2_nav
import com.example.snappet.screens.Trophies_nav
import com.example.snappet.screens.loginStreakNav
import com.example.snappet.screens.menuBottomNav
import com.example.snappet.sign_In.GoogleAuthUiClient
import com.example.snappet.sign_In.LoginScreen2
import com.example.snappet.sign_In.SignInViewModel
import com.example.snappet.ui.theme.SnapPetTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import com.example.snappet.screens.DayInfo
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {

    //private val profileViewModel by viewModels<ProfileViewModel>()

    //criamos aqui o nosso cliente aqui
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializa o ThreeTenABP (para obter as datas)
        AndroidThreeTen.init(this)

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







                    //vai ospedar todos os nossos ecrãns diferentes
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
                                    //navController.navigate(route = Screens.Home.route)
                                    navController.navigate(route = Screens.Streak.route)
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
                                    //navController.navigate(route = Screens.Home.route)
                                    navController.navigate(route = Screens.Streak.route)

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
                            val profileViewModel: ProfileViewModel = viewModel()
                            val database = Firebase.database
                            var userData = googleAuthUiClient.getSignedInUser()
                            LaunchedEffect(Unit) {
                                if (userData != null) {
                                    profileViewModel.fetchUserData(userData.userId)
                                }
                            }
                            val userDataState by profileViewModel.userData.observeAsState()
                            // Check the current value of userDataState whenever it changes
                            println("Curent userDataState: $userDataState")
                            EditProfileScreen2_nav(profileViewModel,navController,
                                //userData = googleAuthUiClient.getSignedInUser(),
                                userData,
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
                            /*
                            val reference = database.getReference ("Users (Quim)")

                            val thisUserRef = userData?.let { it1 -> reference.child(it1.userId) }
                            if (thisUserRef != null) {
                                thisUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // Retrieve the email from the dataSnapshot
                                            val points = dataSnapshot.child("snaPoints").getValue(String::class.java)
                                            if (points != null) {
                                                // Use the retrieved email
                                                userData = userData?.copy(snaPoints = points)!!
                                                //println("AAAAAAAAAAAAAAAAAAAAAAAAAAA: ${userData?.snaPoints}")
                                            }
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle potential errors
                                        println("Error: ${databaseError.message}")
                                    }
                                })
                            }



                            //println("BBBBBBBBBBBBBBBBBBBBBBBBBB: $a")
                            //vamos para o ecrã do Profile
                            //um dos parametros de entrada deste ecrã é o UserData, portanto
                            //vamos busca-lo ao googleAUthUiClient
                            //dantes tinha "ProfileScreen"
                            EditProfileScreen2_nav(navController,
                                //userData = googleAuthUiClient.getSignedInUser(),
                                userData,
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

                             */

                        }

                        //para ir para o Streak login screen
                        composable(route = Screens.Streak.route){
                            val loginStreakViewModel: LoginStreakViewModel = viewModel()
                            var updatedPoints = 0
                            var testeLoginStreak = 1
                            var usersLastLogin = ""

                            val database = Firebase.database
                            val myReference = database.getReference ("Users (Quim)")
                            var userData = googleAuthUiClient.getSignedInUser()
                            LaunchedEffect(Unit) {
                                val userData = googleAuthUiClient.getSignedInUser()
                                if (userData != null) {
                                    loginStreakViewModel.fetchData(userData.userId)
                                }
                            }
                            if (userData != null) {

                                val thisUserRef = myReference.child(userData!!.userId)
                                thisUserRef.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        //se a BD já tem este user não faz nada
                                        if (dataSnapshot.exists()) {
                                        } else {
                                            //caso o user nao exista na BD ele é adicionado à mesma
                                            thisUserRef.child("username").setValue(userData!!.username)
                                            thisUserRef.child("profilePic").setValue(userData!!.profilePictureUrl)
                                            //thisUserRef.child("snaPoints").setValue(userData!!.snaPoints)
                                            //thisUserRef.child("snaPoints").setValue(userData!!.snaPoints)
                                            thisUserRef.child("snaPoints").setValue("5")
                                            thisUserRef.child("LoginStreak").setValue(1)

                                            //buscar a data corrente
                                            //val currentDate = LocalDate.now(ZoneId.systemDefault()).minusDays(1)
                                            val currentDate = LocalDate.now(ZoneId.systemDefault())
                                            //dividie dia, mes e ano
                                            val dayOfMonth = currentDate.dayOfMonth
                                            val monthValue = currentDate.monthValue
                                            val year = currentDate.year

                                            //guardar o dia, mes e ano na RealTimeDataBase (dentro do user)
                                            val dateString = "$dayOfMonth $monthValue $year"
                                            thisUserRef.child("LastLogin").setValue(dateString)
                                            //thisUserRef.child("LastLogin").setValue("1 1 1990")
                                        }
                                    }
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle potential errors
                                        println("Error: ${databaseError.message}")
                                    }
                                })


                                /*
                                val thisUser = myReference.child(userData.userId)
                                thisUser.child("profilePic").setValue(userData.profilePictureUrl)
                                thisUser.child("username").setValue(userData.username)
                                val thisUser2 = myReference.child("9999")
                                thisUser2.child("profilePic").setValue(userData.profilePictureUrl)
                                thisUser2.child("username").setValue(userData.username)
                                */



                                val loginStreakDataState by loginStreakViewModel.loginStreakData.observeAsState()
                                //testeLoginStreak = loginStreakDataState!!
                                val snaPointsDataState by loginStreakViewModel.snaPointsData.observeAsState()
                                val userLastLoginDataState by loginStreakViewModel.lastLoginData.observeAsState()
                                usersLastLogin = userLastLoginDataState.toString()
                                val daysInfo = listOf(
                                    DayInfo("Day: 1", 5),
                                    DayInfo("Day: 2", 5),
                                    DayInfo("Day: 3", 5),
                                    DayInfo("Day: 4", 10),
                                    DayInfo("Day: 5", 10),
                                    DayInfo("Day: 6", 10),
                                    DayInfo("Day: 7", 25)
                                )

                                if (loginStreakDataState != null) {

                                    //buscar a data corrente
                                    val currentDate = LocalDate.now(ZoneId.systemDefault())
                                    //dividie dia, mes e ano
                                    val dayOfMonth = currentDate.dayOfMonth
                                    val monthValue = currentDate.monthValue
                                    val year = currentDate.year
                                    //guardar o dia, mes e ano na RealTimeDataBase (dentro do user)
                                    val dateString = "$dayOfMonth $monthValue $year"

                                    testeLoginStreak = loginStreakDataState as Int

                                    val isBeforeToday = isDateBeforePresentDate(usersLastLogin)

                                    //TESTA ISTO DOS DIAS!
                                    //if (!isBeforeToday) {
                                        //println("********************+$dateString")
                                        //println("####################+$usersLastLogin")
                                        //loginStreakNav(loginStreakViewModel, navController, snaPointsDataState?.toIntOrNull() ?: 0, testeLoginStreak)
                                    //}else {

                                        // Increment by one
                                        testeLoginStreak++

                                        // Check if the value is 8 or above, then reset to 0
                                        if (testeLoginStreak >= 8) {
                                            testeLoginStreak = 1
                                        }


                                        val pointsToAdd = daysInfo.getOrNull(testeLoginStreak!! - 1)?.points ?: 0
                                        val currentPoints = snaPointsDataState?.toIntOrNull() ?: 0
                                        updatedPoints = currentPoints + pointsToAdd

                                        //buscar a data corrente


                                        thisUserRef.updateChildren(
                                            mapOf(
                                                "LastLogin" to dateString,
                                                "snaPoints" to updatedPoints.toString(),
                                                "LoginStreak" to testeLoginStreak,
                                                "username" to (userData?.username ?: ""),
                                                "profilePic" to (userData?.profilePictureUrl ?: "")
                                            )
                                        ).addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                // Update successful
                                                println("User data updated successfully")
                                            } else {
                                                // Update failed
                                                println("Error updating user data: ${task.exception}")
                                            }
                                        }

                                        // Call the composable function here, passing the updatedPoints
                                        loginStreakNav(
                                            loginStreakViewModel,
                                            navController,
                                            updatedPoints,
                                            testeLoginStreak
                                        )
                                    //}
                                } else {
                                    // Display a loading indicator or handle the null state here
                                }


                            }
                            loginStreakNav(loginStreakViewModel,navController,updatedPoints,testeLoginStreak)
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

                    }







                }
            }
        }
    }

    //metodo para comparar 1 data com a data do momento
    //retorna false se a dateString for "hoje"
    //terona true se a dateString tiver acontecido antes de hoje
    fun isDateBeforePresentDate(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("d M yyyy")
        val providedDate = LocalDate.parse(dateString, formatter)
        val currentDate = LocalDate.now()

        return providedDate.isBefore(currentDate)
    }

}