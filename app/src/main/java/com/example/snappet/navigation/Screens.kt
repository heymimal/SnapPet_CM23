package com.example.snappet.navigation

sealed class Screens(val route:String){
    object Login : Screens("sign_in")
    object Home : Screens("home_route")
    object Profile : Screens("profile")
    object Trophies: Screens("trophies_route")
    //object PhotoForm : Screens("photo_form")

    object Camera: Screens("camera")

    object PhotoForm: Screens("photo_form_screen/{capturedImageUri}")

}
