package com.example.snappet.sign_In

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappet.R

@Composable
fun LoginScreen2(
    modifier: Modifier = Modifier,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Login",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .offset(y = 29.dp)
        )

        Spacer(modifier = Modifier.height(150.dp))

        OptionsButton(text = "Sign In With Email", imageId = R.drawable.email_icon)
Spacer(modifier = Modifier.height(16.dp))

        //Botão do Google login
        //Substitui isto:
        //OptionsButton(text = "Sign In With Google", imageId = R.drawable.google_icon)
        //por isto (que em si já estava no método do Ricardo mas assim consigo chamar a função "on SignInClick"):
        // -------------------------------------------------------------------------

        Button(
            onClick = onSignInClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .height(200.dp)
            ) {
                Text(text = "Sign In With Google", color = Color.White)
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Sign In With Google",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        //até aqui ----------------------------------------------------------------------------

Spacer(modifier = Modifier.height(16.dp))

}

Box(
modifier = Modifier.fillMaxSize()
){
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
        modifier = Modifier
            .align(alignment = Alignment.TopStart)
            .offset(
                x = 26.dp,
                y = 680.dp
            )
            .height(50.dp)
            .width(100.dp)

    )
    {
        Text(text = "Back", style = TextStyle(fontSize = 20.sp))
    }
}
}

@Composable
fun OptionsButton(text: String, imageId: Int) {
    Button(
        onClick = { /* TODO */ },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .height(200.dp)
        ) {
            Text(text = text, color = Color.White)
            Image(
                painter = painterResource(id = imageId),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(40.dp))
}