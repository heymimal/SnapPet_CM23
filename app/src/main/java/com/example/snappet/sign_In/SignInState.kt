package com.example.snappet.sign_In

data class SignInState(
    val isSignInSuccessful: Boolean = false,//verifies if login was successfull
    val signInError: String? = null
)