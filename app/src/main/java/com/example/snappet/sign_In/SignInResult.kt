package com.example.snappet.sign_In

data class SignInResult(
    val data: UserData?,//data of the Google acount
    val errorMessage: String?
)

//user data structure
data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val snaPoints: String? = "0"
)
