package com.example.snappet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.snappet.navigation.Screens

@Composable
fun NavGraph(navController : NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
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
        composable("profile_route") {
        }
    }
}
