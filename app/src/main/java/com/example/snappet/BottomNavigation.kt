package com.example.snappet

import android.content.Intent
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.snappet.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
                Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))

        //Text(text = "Hello")
    }
}

@Composable
fun Navigation(navController: NavController){
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home","Map","Camera","Trophy","Profile")
    val routes = listOf("route")

    val context = LocalContext.current

    NavigationBar(
        contentColor = Color(239,139,9)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0
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
            onClick = { selectedItem = 1
                      //navController.navigate(Screens.Map.route)
                    val intent = Intent(context, MapActivity::class.java)
                    context.startActivity(intent)
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
            onClick = { selectedItem = 2
                      val intent = Intent(context, CameraActivity::class.java)

                        context.startActivity(intent)
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
            onClick = { selectedItem = 3
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
            onClick = { selectedItem = 4
                //acrescentei isto para navegar para o profile
                navController.navigate(Screens.PhotoForm.route)},
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