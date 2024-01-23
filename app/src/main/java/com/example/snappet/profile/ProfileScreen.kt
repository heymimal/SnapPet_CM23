package com.example.snappet.profile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.snappet.LocationService
import com.example.snappet.data.Trophy
import com.example.snappet.navigation.Navigation
import com.example.snappet.navigation.Screens
import com.example.snappet.screens.RaritySquare
import com.example.snappet.sign_In.UserData
import com.example.snappet.viewModels.ProfileViewModel
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController : NavHostController,
                  userData: UserData?,
                  onSignOut: () -> Unit
                          ) {
    val userDataState by profileViewModel.userData.observeAsState()
    val trophy by profileViewModel.trophyData.observeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController,4)
        }) {paddingValues ->
        Text(text = "", modifier = Modifier.padding(paddingValues = paddingValues))
        ProfileScreenComposable(userDataState, userData, onSignOut,navController,trophy)
    }
}

@Composable
fun ProfileScreenComposable(
    final: UserData?,
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController,
    trophy: Trophy?,
) {
    var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Edit Profile",
                color = Color.White,
                style = TextStyle(fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .offset(y = 29.dp)
            )
            Spacer(modifier = Modifier.height(100.dp))
            if(userData?.username != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (userData?.profilePictureUrl != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .wrapContentSize(align = Alignment.Center)
                        ) {
                            AsyncImage(
                                model = userData.profilePictureUrl,
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                var permissionsGranted by remember { mutableStateOf(false)}
                val requestMultiplePermissionsLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                        permissionsGranted = !permissions.containsValue(false)
                        Log.d(TAG,"permissions were granted? : $permissionsGranted")
                    }

                val context = LocalContext.current
                var text by remember {
                    mutableStateOf("Start")
                }
                var on by remember {
                    mutableStateOf(false)
                }

                Text(
                    text = userData.username,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .offset(y = 29.dp)
                )
                if (final != null) {
                    Text(
                        text = "SnapPoints: "+final.snaPoints,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .offset(y = 29.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    if (trophy != null) {
                        final.snaPoints?.let { updateTrophy(userData.userId, it.toInt()) }
                    }
                }
            }
            Box(//buttons
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Button(
                        onClick = { /* Ação do botão Update */ },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                        modifier = Modifier
                            .height(50.dp)
                            .width(170.dp)
                    ) {
                        Text(text = "Update", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSignOut,
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                        modifier = Modifier
                            .height(50.dp)
                            .width(170.dp)
                    ) {
                        Text(text = "Logout", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate(route = Screens.Leaderboard.route)
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                        modifier = Modifier
                            .height(50.dp)
                            .width(170.dp)
                    ) {
                        Text(text = "Leaderboard", style = TextStyle(fontSize = 20.sp))
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

// Function to determine the trophy based on points
fun determineTrophy(points: Int): Trophy {
    return when {
        points in 0..49 -> Trophy("bronze", "Novice")
        points in 50..99 -> Trophy("bronze", "Snappy Snapper")
        points in 100..149 -> Trophy("bronze", "Animal Explorer")
        points in 150..199 -> Trophy("bronze", "Pet Paparazzo")
        points in 200..249 -> Trophy("bronze", "Nature Scout")
        points in 250..299 -> Trophy("bronze", "Wildlife Rookie")
        points in 300..349 -> Trophy("silver", "Silver Safari Scout")
        points in 350..399 -> Trophy("silver", "Lens Warrior")
        points in 400..449 -> Trophy("silver", "Animal Whisperer")
        points in 450..499 -> Trophy("silver", "Nature Enthusiast")
        points in 500..549 -> Trophy("silver", "Silver Snapshot Master")
        points in 550..599 -> Trophy("gold", "Gold Wilderness Conquerer")
        points in 600..649 -> Trophy("gold", "Shutter Champion")
        points in 650..699 -> Trophy("gold", "Creature Adventurer")
        points in 700..749 -> Trophy("gold", "Gold Wildlife Expert")
        points in 750..799 -> Trophy("gold", "Gold Photo Virtuoso")
        points in 800..849 -> Trophy("legendary", "Legendary Nature Maestro")
        points in 850..899 -> Trophy("legendary", "Platinum Safari Maestro")
        points in 900..949 -> Trophy("legendary", "Supreme Wildlife Photographer")
        points in 950..999 -> Trophy("legendary", "SnaPet Virtuoso")
        else -> Trophy("legendary", "SnaPet Master")
    }
}

// Function to update user trophy based on points
@Composable
fun updateTrophy(userId: String, points: Int) {
    val trophy = determineTrophy(points)
    val userRef = Firebase.database.getReference("Users (Quim)").child(userId)
    userRef.child("Trophy").setValue(trophy)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 140.dp),
        contentAlignment = Alignment.Center
    ) {
        RaritySquare(
            text = trophy.text,
            trophyType = trophy.trophyType,
            modifier = Modifier
                .padding(start = 0.dp, bottom = 0.dp)
        )
    }
}