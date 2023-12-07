package com.example.snappet.sign_In

data class SignInState(
    //estado que verifica se o sign in foi um sucesso ou não
    //quando for true navega para o proximo screen
    val isSignInSuccessful: Boolean = false,
    //mensagem de erro caso o sign in corra mal
    val signInError: String? = null
)