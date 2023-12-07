package com.example.snappet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.snappet.navigation.Screens
import com.example.snappet.screens.Trophies_nav
import com.example.snappet.screens.menuBottomNav

@Composable
fun NavGraph(navController : NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(route = Screens.Home.route) {

            menuBottomNav(navController)
        }
        composable(route = Screens.Map.route) {
            //map_nav(navController)
            MapActivity()
            //TestActivity()
        }
        composable("camera_route") {

        }
        composable(route = Screens.Trophies.route) {
            Trophies_nav(navController)
        }
        composable("profile_route") {
        }
    }
}
