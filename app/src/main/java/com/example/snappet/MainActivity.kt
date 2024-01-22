package com.example.snappet


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
import com.example.snappet.data.Mission
import com.example.snappet.data.Trophy
import com.example.snappet.data.Photo
import com.example.snappet.screens.DayInfo
import com.example.snappet.screens.TrophiesInfoNav
import com.example.snappet.screens.PhotoDetailScreen
import com.example.snappet.screens.leaderboardNav
import com.example.snappet.viewModels.LeaderboardViewModel
import com.example.snappet.viewModels.LoginStreakViewModel
import com.example.snappet.viewModels.ProfileViewModel
import com.example.snappet.viewModels.ThrophiesViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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
                            var userData = googleAuthUiClient.getSignedInUser()
                            if(userData != null) {
                                CameraClass(navController, userData)
                            }
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
                                            thisUserRef.child("Trophy").setValue(Trophy("bronze", "Novice"))
                                            //thisUserRef.child("LastLogin").setValue("1 1 1990")
                                            createDailyMissions(thisUserRef)
                                            createMonthlyMissions(thisUserRef)
                                            createFullTimeMissions(thisUserRef)

                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle potential errors
                                        //println("Error: ${databaseError.message}")
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

                                    //if the last time the user logged in was in the previous
                                    //month, the monthly missions must be refreshed
                                    if (!isDateInPresentMonth(usersLastLogin)) {
                                        refreshMonthlyMissions(thisUserRef, dateString)
                                    }

                                    if (!isBeforeToday) {
                                        loginStreakNav(
                                            loginStreakViewModel,
                                            navController,
                                            snaPointsDataState?.toIntOrNull() ?: 0,
                                            testeLoginStreak
                                        )
                                    } else {
                                        //novo dia, portanto ha que refrescar as missoes
                                        refreshDailyMissions(thisUserRef,dateString)
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
                        composable(route = Screens.Trophies.route) {
                            val throphiesViewModel: ThrophiesViewModel = viewModel()
                            val userData = googleAuthUiClient.getSignedInUser()

                            // Use LaunchedEffect to fetch data when the composable is first launched
                            LaunchedEffect(userData) {
                                if (userData != null) {
                                    throphiesViewModel.fetchDailyMissions(userData.userId)
                                    throphiesViewModel.fetchMonthlyMissions(userData.userId)
                                    throphiesViewModel.fetchFullTimeMissions(userData.userId)
                                }
                            }

                            // Observe changes in daily, monthly, and full-time missions separately
                            val dailyMissions by throphiesViewModel.missionsData.observeAsState(emptyList())
                            val monthlyMissions by throphiesViewModel.monthlyMissionsData.observeAsState(emptyList())
                            val fullTimeMissions by throphiesViewModel.fullTimeMissionsData.observeAsState(emptyList())

                            // Pass all missions as parameters to TrophiesNav
                            TrophiesNav(navController, dailyMissions ?: emptyList(), monthlyMissions ?: emptyList(), fullTimeMissions ?: emptyList())
                        }

                        composable(route = Screens.TrophiesInfo.route) {
                            TrophiesInfoNav(navController)
                        }

                        composable("${Screens.PhotoDetail.route}{photoId}") { backStackEntry ->
                            // Obtenha o ID da foto da rota
                            val rawPhotoId = backStackEntry.arguments?.getString("photoId") ?: ""
                            val photoId = "-" + rawPhotoId.substringAfter("-")
                            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                            val databaseReference: DatabaseReference = database.reference.child("imagesTest").child(
                                "allImages"
                            )

                            var recentPhotos by remember { mutableStateOf(emptyList<Photo>()) }


                            recentPhotos = getPhotos(databaseReference)

                            val catPhotosr = recentPhotos.filter { it.id == photoId}

                            catPhotosr.forEach { catPhoto ->
                                if(catPhoto.id == photoId){
                                    Log.d(TAG, "Sao iguais!")
                                }
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

    //metodo para comparar 1 data com a data do momento
    //retorna false se a dateString for "hoje"
    //terona true se a dateString tiver acontecido antes de hoje
    fun isDateBeforePresentDate(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("d M yyyy")
        val providedDate = LocalDate.parse(dateString, formatter)
        val currentDate = LocalDate.now()
        return providedDate.isBefore(currentDate)
    }

    //returns true if date is of the present month, false otherwise
    fun isDateInPresentMonth(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("d M yyyy")
        val providedDate = LocalDate.parse(dateString, formatter)
        val currentDate = LocalDate.now()

        // Get the first day of the current month and the first day of the previous month
        val firstDayOfCurrentMonth = currentDate.withDayOfMonth(1)
        val firstDayOfPreviousMonth = firstDayOfCurrentMonth.minusMonths(1)

        // Check if the provided date is after or equal to the first day of the previous month
        // and before the first day of the current month
        return providedDate.isAfter(firstDayOfPreviousMonth) || providedDate.isEqual(firstDayOfPreviousMonth)
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
                        Mission("Bee", 3, 0, 10, "take 3 bee pictures",dateString,false),
                        Mission("Bird", 3, 0, 10, "take 3 bird pictures", dateString,false),
                        Mission("Butterfly", 3, 0, 10, "take 3 butterfly pictures", dateString,false),
                        Mission("Cat", 3, 0, 10, "take 3 cat pictures", dateString,false),
                        Mission("Chicken", 3, 0, 10, "take 3 chicken pictures", dateString,false),
                        Mission("Cow", 3, 0, 10, "take 3 cow pictures", dateString,false),
                        Mission("Dog", 3, 0, 10, "take 3 dog pictures",dateString,false),
                        Mission("Duck", 3, 0, 10, "take 3 duck pictures", dateString,false),
                        Mission("Gecko", 3, 0, 10, "take 3 gecko pictures", dateString,false),
                        Mission("Goat", 3, 0, 10, "take 3 goat pictures", dateString,false),
                        Mission("Horse", 3, 0, 10, "take 3 horse pictures", dateString,false),
                        Mission("Lizard", 3, 0, 10, "take 3 lizard pictures", dateString,false),
                        Mission("Peacock", 3, 0, 10, "take 3 peacock pictures", dateString,false),
                        Mission("Pig", 3, 0, 10, "take 3 pig pictures", dateString,false),
                        Mission("Rabbit", 3, 0, 10, "take 3 rabbit pictures", dateString,false),
                        Mission("Sheep", 3, 0, 10, "take 3 sheep pictures", dateString,false),
                        Mission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString,false)
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
    private fun refreshDailyMissions(thisUserRef: DatabaseReference,dateString: String) {
        // Reference to the "Missions" node
        val missionsReference = thisUserRef.child("DailyMissions")

        // Create and store specific daily missions with the same naming convention
        val newMissionList = listOf(
            Mission("Bee", 3, 0, 10, "take 3 bee pictures",dateString,false),
            Mission("Bird", 3, 0, 10, "take 3 bird pictures", dateString,false),
            Mission("Butterfly", 3, 0, 10, "take 3 butterfly pictures", dateString,false),
            Mission("Cat", 3, 0, 10, "take 3 cat pictures", dateString,false),
            Mission("Chicken", 3, 0, 10, "take 3 chicken pictures", dateString,false),
            Mission("Cow", 3, 0, 10, "take 3 cow pictures", dateString,false),
            Mission("Dog", 3, 0, 10, "take 3 dog pictures",dateString,false),
            Mission("Duck", 3, 0, 10, "take 3 duck pictures", dateString,false),
            Mission("Gecko", 3, 0, 10, "take 3 gecko pictures", dateString,false),
            Mission("Goat", 3, 0, 10, "take 3 goat pictures", dateString,false),
            Mission("Horse", 3, 0, 10, "take 3 horse pictures", dateString,false),
            Mission("Lizard", 3, 0, 10, "take 3 lizard pictures", dateString,false),
            Mission("Peacock", 3, 0, 10, "take 3 peacock pictures", dateString,false),
            Mission("Pig", 3, 0, 10, "take 3 pig pictures", dateString,false),
            Mission("Rabbit", 3, 0, 10, "take 3 rabbit pictures", dateString,false),
            Mission("Sheep", 3, 0, 10, "take 3 sheep pictures", dateString,false),
            Mission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString,false)
        )

        // Replace existing missions with the new ones
        for ((index, mission) in newMissionList.withIndex()) {
            missionsReference.child("DailyMission${index + 1}").setValue(mission)
        }
    }

    private fun createMonthlyMissions(thisUserRef: DatabaseReference) {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfMonth = currentDate.dayOfMonth
        val monthValue = currentDate.monthValue
        val year = currentDate.year
        val dateString = "$dayOfMonth $monthValue $year"
        thisUserRef.child("LastLogin").setValue(dateString)
        val missionsReference = thisUserRef.child("MonthlyMissions")
        missionsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Create and store specific monthly missions
                    val missionList = listOf(
                        Mission("Bee", 30, 0, 40, "take 30 bee pictures",dateString,false),
                        Mission("Bird", 30, 0, 40, "take 30 bird pictures", dateString,false),
                        Mission("Butterfly", 30, 0, 40, "take 30 butterfly pictures", dateString,false),
                        Mission("Cat", 30, 0, 40, "take 30 cat pictures", dateString,false),
                        Mission("Chicken", 30, 0, 40, "take 30 chicken pictures", dateString,false),
                        Mission("Cow", 30, 0, 40, "take 30 cow pictures", dateString,false),
                        Mission("Dog", 30, 0, 40, "take 30 dog pictures",dateString,false),
                        Mission("Duck", 30, 0, 40, "take 30 duck pictures", dateString,false),
                        Mission("Gecko", 30, 0, 40, "take 30 gecko pictures", dateString,false),
                        Mission("Goat", 30, 0, 40, "take 30 goat pictures", dateString,false),
                        Mission("Horse", 30, 0, 40, "take 30 horse pictures", dateString,false),
                        Mission("Lizard", 30, 0, 40, "take 30 lizard pictures", dateString,false),
                        Mission("Peacock", 30, 0, 40, "take 30 peacock pictures", dateString,false),
                        Mission("Pig", 30, 0, 40, "take 30 pig pictures", dateString,false),
                        Mission("Rabbit", 30, 0, 40, "take 30 rabbit pictures", dateString,false),
                        Mission("Sheep", 30, 0, 40, "take 30 sheep pictures", dateString,false),
                        Mission("100PicturesMission", 100, 0, 120, "take 100 pictures", dateString,false)
                    )

                    // Add each mission under the "Missions" node with a custom key
                    for ((index, mission) in missionList.withIndex()) {
                        missionsReference.child("MonthlyMission${index + 1}").setValue(mission)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Refreshes the monthly missions
    private fun refreshMonthlyMissions(thisUserRef: DatabaseReference, dateString: String) {
        // Reference to the "Missions" node
        val missionsReference = thisUserRef.child("MonthlyMissions")

        // Create and store specific monthly missions with the same naming convention
        val newMissionList = listOf(
            Mission("Bee", 30, 0, 40, "take 30 bee pictures",dateString,false),
            Mission("Bird", 30, 0, 40, "take 30 bird pictures", dateString,false),
            Mission("Butterfly", 30, 0, 40, "take 30 butterfly pictures", dateString,false),
            Mission("Cat", 30, 0, 40, "take 30 cat pictures", dateString,false),
            Mission("Chicken", 30, 0, 40, "take 30 chicken pictures", dateString,false),
            Mission("Cow", 30, 0, 40, "take 30 cow pictures", dateString,false),
            Mission("Dog", 30, 0, 40, "take 30 dog pictures",dateString,false),
            Mission("Duck", 30, 0, 40, "take 30 duck pictures", dateString,false),
            Mission("Gecko", 30, 0, 40, "take 30 gecko pictures", dateString,false),
            Mission("Goat", 30, 0, 40, "take 30 goat pictures", dateString,false),
            Mission("Horse", 30, 0, 40, "take 30 horse pictures", dateString,false),
            Mission("Lizard", 30, 0, 40, "take 30 lizard pictures", dateString,false),
            Mission("Peacock", 30, 0, 40, "take 30 peacock pictures", dateString,false),
            Mission("Pig", 30, 0, 40, "take 30 pig pictures", dateString,false),
            Mission("Rabbit", 30, 0, 40, "take 30 rabbit pictures", dateString,false),
            Mission("Sheep", 30, 0, 40, "take 30 sheep pictures", dateString,false),
            Mission("100PicturesMission", 100, 0, 120, "take 100 pictures", dateString,false)
        )

        // Replace existing missions with the new ones
        for ((index, mission) in newMissionList.withIndex()) {
            missionsReference.child("MonthlyMission${index + 1}").setValue(mission)
        }
    }

    private fun createFullTimeMissions(thisUserRef: DatabaseReference) {
        val currentDate = LocalDate.now(ZoneId.systemDefault())
        val dayOfMonth = currentDate.dayOfMonth
        val monthValue = currentDate.monthValue
        val year = currentDate.year
        val dateString = "$dayOfMonth $monthValue $year"
        thisUserRef.child("LastLogin").setValue(dateString)
        val missionsReference = thisUserRef.child("FullTimeMissions")
        missionsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Create and store specific daily missions
                    val missionList = listOf(
                        Mission("Bee", 300, 0, 500, "take 300 bee pictures",dateString,false),
                        Mission("Bird", 300, 0, 500, "take 300 bird pictures", dateString,false),
                        Mission("Butterfly", 300, 0, 500, "take 300 butterfly pictures", dateString,false),
                        Mission("Cat", 300, 0, 500, "take 300 cat pictures", dateString,false),
                        Mission("Chicken", 300, 0, 500, "take 300 chicken pictures", dateString,false),
                        Mission("Cow", 300, 0, 500, "take 300 cow pictures", dateString,false),
                        Mission("Dog", 300, 0, 500, "take 300 dog pictures",dateString,false),
                        Mission("Duck", 300, 0, 500, "take 300 duck pictures", dateString,false),
                        Mission("Gecko", 300, 0, 500, "take 300 gecko pictures", dateString,false),
                        Mission("Goat", 300, 0, 500, "take 300 goat pictures", dateString,false),
                        Mission("Horse", 300, 0, 500, "take 300 horse pictures", dateString,false),
                        Mission("Lizard", 300, 0, 500, "take 300 lizard pictures", dateString,false),
                        Mission("Peacock", 300, 0, 500, "take 300 peacock pictures", dateString,false),
                        Mission("Pig", 300, 0, 500, "take 300 pig pictures", dateString,false),
                        Mission("Rabbit", 300, 0, 500, "take 300 rabbit pictures", dateString,false),
                        Mission("Sheep", 300, 0, 500, "take 300 sheep pictures", dateString,false),
                        Mission("1000PicturesMission", 1000, 0, 1500, "take 1000 pictures", dateString,false)
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
}

