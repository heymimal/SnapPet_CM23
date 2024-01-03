package com.example.snappet.navigation

sealed class Screens(val route:String){
    //criei aqui a route
    object Login : Screens("sign_in")
    object Streak : Screens ("login_streak")

    object Home : Screens("home_route")
    object Map : Screens("map_route")
    object Profile : Screens("profile_route")
    object Trophies: Screens("trophies_route")

    object Camera: Screens("camera_route")

}
