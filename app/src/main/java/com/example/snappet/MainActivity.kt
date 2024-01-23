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
import com.example.snappet.data.MonthAnimal
import com.example.snappet.screens.DayInfo
import com.example.snappet.screens.FirstScreenNav
import com.example.snappet.screens.MonthAnimalNav
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

    private val googleAuthUiClient by lazy { //client creation
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

    //manages signing user to its Google account
    @Composable
    private fun SignIn(navController: NavHostController) {
        val viewModel = viewModel<SignInViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = Unit) { //verify if its loged in already
            if (googleAuthUiClient.getSignedInUser() != null) { //user is valid and exists
                navController.navigate(route = Screens.Streak.route)
            }
        }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->//activityResult
                if (result.resultCode == RESULT_OK) {
                    lifecycleScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
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
                viewModel.resetState()
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
        AndroidThreeTen.init(this)//inicialize ThreeTenABP (used to get dates)
        val context = this
        setContent {
            SnapPetTheme {
                val navController = rememberNavController()//navController reference
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = Screens.FirstScreen.route) {//manages navigation
                        composable("sign_in") {
                            SignIn(navController = navController)
                        }

                        composable(Screens.Profile.route) {
                            val profileViewModel: ProfileViewModel = viewModel()
                            var userData = googleAuthUiClient.getSignedInUser()
                            LaunchedEffect(Unit) {
                                if (userData != null) {
                                    profileViewModel.fetchUserData(userData.userId)
                                }
                            }
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

                        composable(route = Screens.Streak.route) {//login streak functionality
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
                                        if (dataSnapshot.exists()) {
                                        } else {//if the user doesnt exist in the DB
                                            thisUserRef.child("username").setValue(userData!!.username)
                                            thisUserRef.child("profilePic").setValue(userData!!.profilePictureUrl)
                                            thisUserRef.child("snaPoints").setValue("5")
                                            thisUserRef.child("LoginStreak").setValue(1)
                                            val currentDate = LocalDate.now(ZoneId.systemDefault())
                                            val dayOfMonth = currentDate.dayOfMonth
                                            val monthValue = currentDate.monthValue
                                            val year = currentDate.year
                                            val dateString = "$dayOfMonth $monthValue $year"
                                            thisUserRef.child("LastLogin").setValue(dateString)//save users last login
                                            thisUserRef.child("Trophy").setValue(Trophy("bronze", "Novice"))//inicial trophy
                                            createDailyMissions(thisUserRef)
                                            createMonthlyMissions(thisUserRef)
                                            createFullTimeMissions(thisUserRef)
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                    }
                                })
                                val loginStreakDataState by loginStreakViewModel.loginStreakData.observeAsState()
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
                                    val currentDate = LocalDate.now(ZoneId.systemDefault())
                                    val dayOfMonth = currentDate.dayOfMonth
                                    val monthValue = currentDate.monthValue
                                    val year = currentDate.year
                                    val dateString = "$dayOfMonth $monthValue $year"
                                    testeLoginStreak = loginStreakDataState as Int
                                    val isBeforeToday = isDateBeforePresentDate(usersLastLogin)
                                    if (!isDateInPresentMonth(usersLastLogin)) {//if the last time the user logged in was in the previous month, the monthly missions must be refreshed
                                        refreshMonthlyMissions(thisUserRef, dateString)
                                    }
                                    if (!isBeforeToday) {
                                        loginStreakNav(
                                            loginStreakViewModel,
                                            navController,
                                            snaPointsDataState?.toIntOrNull() ?: 0,
                                            testeLoginStreak
                                        )
                                    } else {//if the users last login was before today
                                        refreshDailyMissions(thisUserRef,dateString)//refresh daily missions
                                        testeLoginStreak++
                                        if (testeLoginStreak >= 8) {//7 days of the week
                                            testeLoginStreak = 1
                                        }
                                        val pointsToAdd =daysInfo.getOrNull(testeLoginStreak!! - 1)?.points ?: 0
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
                                            } else {
                                            }
                                        }
                                        loginStreakNav(
                                            loginStreakViewModel,
                                            navController,
                                            updatedPoints,
                                            testeLoginStreak
                                        )
                                    }
                                } else {
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
                                leaderboardViewModel.fetchLeaderboardData()// Fetch leaderboard data here
                            }
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
                            TrophiesNav(navController, dailyMissions ?: emptyList(), monthlyMissions ?: emptyList(), fullTimeMissions ?: emptyList())
                        }

                        composable(route = Screens.TrophiesInfo.route) {
                            TrophiesInfoNav(navController)
                        }

                        composable(route = Screens.MonthAnimal.route) {
                            val monthAnimal = MonthAnimal()
                            val presentMonthAnimal = monthAnimal.getAnimalForPresentMonth()
                            MonthAnimalNav(navController,presentMonthAnimal)
                        }

                        composable(route = Screens.FirstScreen.route) {
                            FirstScreenNav(navController)
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

    //returns false if dateString is from "today", false otherwise
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
        val firstDayOfCurrentMonth = currentDate.withDayOfMonth(1)// Get the first day of the current month and the first day of the previous month
        val firstDayOfPreviousMonth = firstDayOfCurrentMonth.minusMonths(1)
        return providedDate.isAfter(firstDayOfPreviousMonth) || providedDate.isEqual(firstDayOfPreviousMonth)// Check if the provided date is after or equal to the first day of the previous month and before the first day of the current month
    }

    //creats the first 5 random daily missions
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
                    val missionList = listOf(
                        Mission("Bee", 3, 0, 10, "take 3 bee pictures", dateString, false),
                        Mission("Bird", 3, 0, 10, "take 3 bird pictures", dateString, false),
                        Mission("Butterfly", 3, 0, 10, "take 3 butterfly pictures", dateString, false),
                        Mission("Cat", 3, 0, 10, "take 3 cat pictures", dateString, false),
                        Mission("Chicken", 3, 0, 10, "take 3 chicken pictures", dateString, false),
                        Mission("Cow", 3, 0, 10, "take 3 cow pictures", dateString, false),
                        Mission("Dog", 3, 0, 10, "take 3 dog pictures", dateString, false),
                        Mission("Duck", 3, 0, 10, "take 3 duck pictures", dateString, false),
                        Mission("Gecko", 3, 0, 10, "take 3 gecko pictures", dateString, false),
                        Mission("Goat", 3, 0, 10, "take 3 goat pictures", dateString, false),
                        Mission("Horse", 3, 0, 10, "take 3 horse pictures", dateString, false),
                        Mission("Lizard", 3, 0, 10, "take 3 lizard pictures", dateString, false),
                        Mission("Peacock", 3, 0, 10, "take 3 peacock pictures", dateString, false),
                        Mission("Pig", 3, 0, 10, "take 3 pig pictures", dateString, false),
                        Mission("Rabbit", 3, 0, 10, "take 3 rabbit pictures", dateString, false),
                        Mission("Sheep", 3, 0, 10, "take 3 sheep pictures", dateString, false),
                        Mission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString, false)
                    )
                    val shuffledMissions = missionList.filter { it != missionList.last() }.shuffled().take(4)// Shuffle the missionList excluding "10PicturesMission" and take the first 4 missions
                    val finalMissions = shuffledMissions + missionList.last()// Add "10PicturesMission" to the end of the list
                    for ((index, mission) in finalMissions.withIndex()) {// Add each mission to the "DailyMissions" node with a custom key
                        missionsReference.child("DailyMission${index + 1}").setValue(mission)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    // refreshes the 5 daily missions
    private fun refreshDailyMissions(thisUserRef: DatabaseReference, dateString: String) {
        val missionsReference = thisUserRef.child("DailyMissions")
        val missionList = listOf(
            Mission("Bee", 3, 0, 10, "take 3 bee pictures", dateString, false),
            Mission("Bird", 3, 0, 10, "take 3 bird pictures", dateString, false),
            Mission("Butterfly", 3, 0, 10, "take 3 butterfly pictures", dateString, false),
            Mission("Cat", 3, 0, 10, "take 3 cat pictures", dateString, false),
            Mission("Chicken", 3, 0, 10, "take 3 chicken pictures", dateString, false),
            Mission("Cow", 3, 0, 10, "take 3 cow pictures", dateString, false),
            Mission("Dog", 3, 0, 10, "take 3 dog pictures", dateString, false),
            Mission("Duck", 3, 0, 10, "take 3 duck pictures", dateString, false),
            Mission("Gecko", 3, 0, 10, "take 3 gecko pictures", dateString, false),
            Mission("Goat", 3, 0, 10, "take 3 goat pictures", dateString, false),
            Mission("Horse", 3, 0, 10, "take 3 horse pictures", dateString, false),
            Mission("Lizard", 3, 0, 10, "take 3 lizard pictures", dateString, false),
            Mission("Peacock", 3, 0, 10, "take 3 peacock pictures", dateString, false),
            Mission("Pig", 3, 0, 10, "take 3 pig pictures", dateString, false),
            Mission("Rabbit", 3, 0, 10, "take 3 rabbit pictures", dateString, false),
            Mission("Sheep", 3, 0, 10, "take 3 sheep pictures", dateString, false),
            Mission("10PicturesMission", 10, 0, 40, "take 10 pictures", dateString, false)
        )
        val shuffledMissions = missionList.filter { it != missionList.last() }.shuffled().take(4)// Shuffle the missionList excluding "10PicturesMission" and take the first 4 missions
        val finalMissions = shuffledMissions + missionList.last()// Add "10PicturesMission" to the end of the list
        for ((index, mission) in finalMissions.withIndex()) {// Replace existing missions with the new ones
            missionsReference.child("DailyMission${index + 1}").setValue(mission)
        }
    }
    //creates the first monthly missions
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
                    for ((index, mission) in missionList.withIndex()) {
                        missionsReference.child("MonthlyMission${index + 1}").setValue(mission)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    // Refreshes the monthly missions
    private fun refreshMonthlyMissions(thisUserRef: DatabaseReference, dateString: String) {
        val missionsReference = thisUserRef.child("MonthlyMissions")
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
        for ((index, mission) in newMissionList.withIndex()) {// Replace existing missions with the new ones
            missionsReference.child("MonthlyMission${index + 1}").setValue(mission)
        }
    }

    //creates the full time missions (dont need to be refreshed)
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
                    for ((index, mission) in missionList.withIndex()) {// Add each mission under the "Missions" node with a custom key
                        missionsReference.child("DailyMission${index + 1}").setValue(mission)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}

