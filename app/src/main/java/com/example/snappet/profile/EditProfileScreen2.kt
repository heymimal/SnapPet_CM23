package com.example.snappet.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.snappet.Navigation
import com.example.snappet.R
import com.example.snappet.sign_In.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen2_nav(navController : NavController,
                          userData: UserData?,
    //queremos ter um lambeda quando ele fizer sign out
    //ele recebe esta função como paramentro de entrada
                          onSignOut: () -> Unit
                          ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        EditProfileScreen2(userData, onSignOut)

        //Text(text = "Hello")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
//para este Screen não precisamos de um view model pois ele não
// contém nenhum estado nem nada que "mude" ao longo do tempo
//só mostra user data estática
fun EditProfileScreen2(
    userData: UserData?,
    //queremos ter um lambeda quando ele fizer sign out
    //ele recebe esta função como paramentro de entrada
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()

        //substitui isto
        //EditProfile()
        //por isto:------------------------------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Edit Profile",
                color = Color.Black,
                style = TextStyle(fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .offset(y = 29.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))


            /*Text(
                text = "Profile Image",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 20.sp)
            )*/
            //Texto para o username
            //se o userData não tiver um user name que seja nulo
            if(userData?.username != null) {
                Text(
                    //mostra o username
                    text = userData.username,
                    //o texto vai ser centrado
                    textAlign = TextAlign.Center,
                    //tamanho do texto
                    fontSize = 20.sp,
                    //por negrito no texto
                    fontWeight = FontWeight.SemiBold
                )
                //espacinho depois do username
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    .padding(start = 0.dp, end = 16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ){
                if(userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        //descrição da imagem
                        contentDescription = "Profile picture",
                        //o tamanho que a imagem vai ter
                        modifier = Modifier
                            .size(120.dp)
                            //clipar a imagem para uma forma circular
                            .clip(CircleShape),
                        //faz crop da imagem (retira porções indesejadas da imagem)
                        contentScale = ContentScale.Crop
                    )
                }
                //em vez desta imagem user o AsyncImage de cima), mas mantive as dimensões
                /*Image(
                    painter = painterResource(id = R.drawable.profilepic),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .padding(end = 16.dp)
                )*/

                Button(
                    onClick = { /* Ação do botão Update */ },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(text = "Update")
                }

                //Botão para logout
                Button(
                    onClick = onSignOut,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(text = "Logout")
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            TextField(
                value = "",
                onValueChange = {/*TODO*/},
                label = {Text("Username")},
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(30.dp))

            TextField(
                value = "",
                onValueChange = {/*TODO*/},
                label = {Text("Password")},
                modifier = Modifier.fillMaxWidth())

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
                        x = 246.dp,
                        y = 680.dp
                    )
                    .height(50.dp)
                    .width(100.dp)

            )
            {
                Text(text = "Next", style = TextStyle(fontSize = 20.sp))
            }
        }
        //até aqui:-------------------------------------------------------------------------------------

        //Text(text = "Hello")

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Edit Profile",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .offset(y = 29.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))


        Text(
            text = "Profile Image",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            //horizontalArrangement = Arrangement.SpaceBetween,
            .padding(start = 0.dp, end = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(id = R.drawable.profilepic),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .padding(end = 16.dp)
            )

            Button(
                onClick = { /* Ação do botão Update */ },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = "Update")
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = "",
            onValueChange = {/*TODO*/},
            label = {Text("Username")},
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = "",
            onValueChange = {/*TODO*/},
            label = {Text("Password")},
            modifier = Modifier.fillMaxWidth())

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
                    x = 246.dp,
                    y = 680.dp
                )
                .height(50.dp)
                .width(100.dp)

        )
        {
            Text(text = "Next", style = TextStyle(fontSize = 20.sp))
        }
    }
}