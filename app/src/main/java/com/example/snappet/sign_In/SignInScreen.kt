package com.example.snappet.sign_In

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

//Ecrã de login
@Composable
fun SignInScreen(
    //recebe um SignInState para o UI ser atualizado corretamente
    state: SignInState,
    //função que não recebe parametro de entrada nenhum e devolve um Unit (um void)
    onSignInClick: () -> Unit
) {
    //esta parte a seguir até ao fim do LauncherEffect mostra um Toast
    //caso haja algum erro de login

    //aqui guardamos o contexto (chave que nos permite aceder a várias coisas [imagens,texto,layouts] da app de Android) da função composable
    val context = LocalContext.current
    //chamado quando o "signInError" do state mudar
    LaunchedEffect(key1 = state.signInError) {
        //verifica se realmente há um signInError
        state.signInError?.let { error ->
            //caso haja um signInError mostra um Toast
            Toast.makeText(
                //passamos o contexto lá para dentro
                context,
                //e a mensagem de erro
                error,
                //um Toast longo
                Toast.LENGTH_LONG
                //mostra o Toast
            ).show()
        }
    }

    //resto do ecrã

    Box(
        modifier = Modifier
            //a box enche o ecrã todo
            .fillMaxSize()
            //margens à volta da box
            .padding(16.dp),
        //o conteudo da box está centrado
        contentAlignment = Alignment.Center
    ) {
        //butão para login
        //quando o botão é clicado ele corre a função "onSignInClick" (todo desta classe)
        Button(onClick = onSignInClick) {
            Text(text = "Sign in")
        }
    }
}