package com.example.snappet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.snappet.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
                Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "stuff", modifier = Modifier.padding(paddingValues = paddingValues))
        //Text(text = "Hello")
    }
}

@Composable
fun Navigation(currentDestination : NavDestination?, navController: NavHostController){
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home","Map","Camera","Trophy","Profile")
    val routes = listOf("route")
    NavigationBar(
        contentColor = Color(239,139,9)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 },
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
            onClick = { selectedItem = 1 },
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
            onClick = { selectedItem = 2 },
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
            onClick = { selectedItem = 3 },
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
            onClick = { selectedItem = 4 },
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



@Preview(showBackground = true)
@Composable
fun Preview(){
    BottomNavigationBar()
}