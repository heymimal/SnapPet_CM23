package com.example.snappet.navigation

sealed class Screens(val route:String){
    //criei aqui a route
    object Login : Screens("sign_in")
    object Streak : Screens ("login_streak")
    object Leaderboard : Screens ("leaderboard_route")

    object Home : Screens("home_route")
    object Profile : Screens("profile")
    object Trophies: Screens("trophies_route")

    object TrophiesInfo: Screens("trophiesInfo_route")

    object MonthAnimal: Screens("monthAnimal_route")

    object FirstScreen: Screens("firstScreen_route")

    object Camera: Screens("camera")

    object PhotoDetail: Screens("photo_detail/{photoId}")

}
