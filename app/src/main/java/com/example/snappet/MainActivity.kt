package com.example.snappet


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
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
import androidx.compose.runtime.DisposableEffect
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
import com.example.snappet.screens.HomeMenu
import com.example.snappet.screens.TrophiesNav
import com.example.snappet.profile.ProfileScreen
import com.example.snappet.screens.loginStreakNav
import com.example.snappet.sign_In.GoogleAuthUiClient
import com.example.snappet.sign_In.LoginScreen
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.snappet.data.DailyMission
import com.example.snappet.data.Photo
import com.example.snappet.screens.DayInfo
import com.example.snappet.screens.PhotoDetailScreen
import com.example.snappet.screens.leaderboardNav
import com.example.snappet.viewModels.LeaderboardViewModel
import com.example.snappet.viewModels.LoginStreakViewModel
import com.example.snappet.viewModels.ProfileViewModel
import com.example.snappet.viewModels.ThrophiesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resumeWithException


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy { //criamos aqui o nosso cliente aqui
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private fun onSignout(navController: NavHostController) {
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
    private fun SignIn(navController: NavHostController) {
        val viewModel = viewModel<SignInViewModel>()

        val state by viewModel.state.collectAsStateWithLifecycle()//referencia do nosso state que podemos buscar
        //agora o nosso "state" recebe os updates

        LaunchedEffect(key1 = Unit) { //verifica se o user está loged in already
            if (googleAuthUiClient.getSignedInUser() != null) { //user is valid and exists
                navController.navigate(route = Screens.Streak.route)
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            //vamos receber uma activityResult, neste caso vamos dar-lhe o nome de "result"
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
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

        LaunchedEffect(key1 = state.isSignInSuccessful) {
            if (state.isSignInSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Sign in successful",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(route = Screens.Streak.route)
                //navController.navigate("Camera")

                viewModel.resetState() //reset do state view model para dar login outra vez
            }
        }
        LoginScreen(
            state = state,
            onSignInClick = {
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
                    NavHost(navController = navController, startDestination = "sign_in") {
                        //este é o composable mostrado no sign in screen
                        //temos que dar a routh ao composable
                        composable("sign_in") {
                            SignIn(navController = navController)
                        }

                        composable(Screens.Profile.route) { //agora é para o profile
                            val profileViewModel: ProfileViewModel = viewModel()
                            var userData = googleAuthUiClient.getSignedInUser()
                            LaunchedEffect(Unit) {
                                if (userData != null) {
                                    profileViewModel.fetchUserData(userData.userId)
                                }
                            }
                            val userDataState by profileViewModel.userData.observeAsState()
                            ProfileScreen(
                                profileViewModel = profileViewModel,
                                navController = navController,
                                userData = userData,
                                onSignOut = { onSignout(navController) }
                            )
                        }
                        composable("Camera") {
                            CameraClass(navController)
                        }
                        //para ir para o Streak login screen
                        composable(route = Screens.Streak.route) {
                            val loginStreakViewModel: LoginStreakViewModel = viewModel()
                            var updatedPoints = 0
                            var testeLoginStreak = 1
                            var usersLastLogin = ""
                            val database = Firebase.database
                            val myReference = database.getReference("Users (Quim)")
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
                                            thisUserRef.child("username")
                                                .setValue(userData!!.username)
                                            thisUserRef.child("profilePic")
                                                .setValue(userData!!.profilePictureUrl)
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
                                            createDailyMissions(thisUserRef)

                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle potential errors
                                        println("Error: ${databaseError.message}")
                                    }
                                })

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
                                    if (!isBeforeToday) {
                                        loginStreakNav(
                                            loginStreakViewModel,
                                            navController,
                                            snaPointsDataState?.toIntOrNull() ?: 0,
                                            testeLoginStreak
                                        )
                                    } else {
                                        //novo dia, portanto ha que refrescar as missoes
                                        refreshMissions(thisUserRef,dateString)
                                        // Increment by one
                                        testeLoginStreak++
                                        // Check if the value is 8 or above, then reset to 0
                                        if (testeLoginStreak >= 8) {
                                            testeLoginStreak = 1
                                        }
                                        val pointsToAdd =
                                            daysInfo.getOrNull(testeLoginStreak!! - 1)?.points ?: 0
                                        val currentPoints = snaPointsDataState?.toIntOrNull() ?: 0
                                        updatedPoints = currentPoints + pointsToAdd

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
                                    }
                                } else {
                                    // Display a loading indicator or handle the null state here
                                }
                            }
                            loginStreakNav(
                                loginStreakViewModel,
                                navController,
                                updatedPoints,
                                testeLoginStreak
                            )
                        }
                        composable(route = Screens.Leaderboard.route) {
                            val leaderboardViewModel: LeaderboardViewModel = viewModel()

                            LaunchedEffect(Unit) {
                                // Fetch leaderboard data here
                                leaderboardViewModel.fetchLeaderboardData()
                            }

                            // Call the composable function here, passing the ViewModel
                            leaderboardNav(
                                navController,
                                leaderboardViewModel.leaderboardData.value ?: emptyList()
                            )
                        }

                        composable(route = Screens.Home.route) {
                            HomeMenu(navController)
                        }
                        composable("map_route") {
                        }
                        composable("camera_route") {

                        }
                        composable(route = Screens.Trophies.route) {
                            val throphiesViewModel: ThrophiesViewModel = viewModel()
                            val userData = googleAuthUiClient.getSignedInUser()

                            LaunchedEffect(Unit) {
                                if (userData != null) {
                                    throphiesViewModel.fetchDailyMissions(userData.userId)
                                }
                            }
                            val dailyMissions by throphiesViewModel.dailyMissionsData.observeAsState(emptyList())
                            TrophiesNav(navController, dailyMissions ?: emptyList())
                        }

                        composable("${Screens.PhotoDetail.route}{photoId}") { backStackEntry ->
                            // Obtenha o ID da foto da rota
                            val rawPhotoId = backStackEntry.arguments?.getString("photoId") ?: ""
                            val photoId = "-" + rawPhotoId.substringAfter("-")
                            //Log.d(TAG, "PHOTO ID TOTAL:: " + photoId)
                            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                            val databaseReference: DatabaseReference = database.reference.child("imagesTest").child(
                                "allImages"
                            )

                            var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

                            //val photo = remember { mutableStateOf<Photo?>(null) }
                            Log.d(TAG, "ATAO1")

                            recentPhotos = getPhotos(databaseReference)

                            val catPhotos = recentPhotos.filter { it.animalType == "cat" }
                            val catPhotosr = recentPhotos.filter { it.id == photoId}

                            Log.d(TAG, "tamanho 0? " + catPhotos.size)
                            Log.d(TAG, "tamanho real? " + catPhotosr.size)

                            catPhotosr.forEach { catPhoto ->
                                if(catPhoto.id == photoId){
                                    Log.d(TAG, "Sao iguais!")
                                }
                                // Add more details as needed

                                PhotoDetailScreen(catPhoto, navController)
                            }
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun getPhotos(databaseReference: DatabaseReference): List<Photo>{

        var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }

        LaunchedEffect(key1 = databaseReference) {
            Log.d(TAG, "ATAO2")
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


                        imageUrl?.let {
                            val photo = Photo(
                                imageUri = Uri.parse(it),
                                animalType = animalType ?: "",
                                contextPhoto = contextPhoto ?: "",
                                description = description ?: "",
                                id = id ?: "",
                                downloadUrl = downloadUrl?: "",
                                sender = sender?: ""
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

    //metodo para comparar 1 data com a data do momento
    //retorna false se a dateString for "hoje"
    //terona true se a dateString tiver acontecido antes de hoje
    fun isDateBeforePresentDate(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("d M yyyy")
        val providedDate = LocalDate.parse(dateString, formatter)
        val currentDate = LocalDate.now()
        return providedDate.isBefore(currentDate)
    }

    //creats the 4 first daily missions
    private fun createDailyMissions(thisUserRef: DatabaseReference) {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfMonth = currentDate.dayOfMonth
        val monthValue = currentDate.monthValue
        val year = currentDate.year
        val dateString = "$dayOfMonth $monthValue $year"
        thisUserRef.child("LastLogin").setValue(dateString)
        val missionsReference = thisUserRef.child("DailyMissions")
        missionsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Create and store specific daily missions
                    val missionList = listOf(
                        DailyMission("DogMission", 3, 0, 10, "take 3 dog pictures",dateString),
                        DailyMission("CatMission", 3, 0, 10, "take 3 cat pictures", dateString),
                        DailyMission("BirdMission", 3, 0, 10, "take 3 bird pictures", dateString),
                        DailyMission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString)
                    )

                    // Add each mission under the "Missions" node with a custom key
                    for ((index, mission) in missionList.withIndex()) {
                        missionsReference.child("DailyMission${index + 1}").setValue(mission)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    // refreshes the 4 daily missions
    private fun refreshMissions(thisUserRef: DatabaseReference,dateString: String) {
        // Reference to the "Missions" node
        val missionsReference = thisUserRef.child("DailyMissions")

        // Create and store specific daily missions with the same naming convention
        val newMissionList = listOf(
            DailyMission("DogMission", 3, 0, 10, "take 3 dog pictures",dateString),
            DailyMission("CatMission", 3, 0, 10, "take 3 cat pictures", dateString),
            DailyMission("BirdMission", 3, 0, 10, "take 3 bird pictures", dateString),
            DailyMission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString)
        )

        // Replace existing missions with the new ones
        for ((index, mission) in newMissionList.withIndex()) {
            missionsReference.child("DailyMission${index + 1}").setValue(mission)
        }
    }
}

