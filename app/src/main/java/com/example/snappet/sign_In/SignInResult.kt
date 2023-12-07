package com.example.snappet.sign_In

data class SignInResult(
    //o que contém a data da conta Google do user
    val data: UserData?,
    //mensagem de erro caso alguma coisa corra mal
    val errorMessage: String?
)

//estrutura de uma user data: ID, username e photo
data class UserData(
    val userId: String,
    val username: String?,
    //o "?" significa nullable state (nem todos os users podem ter foto)
    val profilePictureUrl: String?
)
