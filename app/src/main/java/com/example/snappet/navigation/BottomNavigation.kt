package com.example.snappet.navigation

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
//import com.example.snappet.activity.CameraActivity
import com.example.snappet.R
import com.example.snappet.TestMapActivity
import com.google.firebase.appcheck.internal.util.Logger.TAG

@Composable
fun Navigation(navController: NavHostController, selectedItem : Int){
    val items = listOf("Home","Map","Camera","Trophy","Profile")

    val context = LocalContext.current

    Log.d(TAG,navController.graph.toString())

    NavigationBar(
        contentColor = Color(239,139,9)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = {
                navController.navigate(Screens.Home.route)
                      },
            label = {
                Text("Home")
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_home_24),
                    contentDescription = items[0],
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = {
                    context.startActivity(Intent(context, TestMapActivity::class.java))
                      },
            label = {
                Text("Map")
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_map_24),
                    contentDescription = items[1]
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = {
                navController.navigate("Camera")
                      },
            label = {
                Text("Camera")
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = items[2]
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == 3,
            onClick = {
                navController.navigate(Screens.Trophies.route)},
            label = {
                Text("Trophy")
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.baseline_trophy_24),
                    contentDescription = items[3]
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == 4,
            onClick = {
                //acrescentei isto para navegar para o profile
                navController.navigate(Screens.Profile.route)},
            label = {
                Text("Profile")
            },
            icon = {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = items[4]
                )
            }
        )
    }
}