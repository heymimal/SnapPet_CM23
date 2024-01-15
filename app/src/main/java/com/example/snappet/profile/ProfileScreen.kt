package com.example.snappet.profile

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.snappet.navigation.Navigation
import com.example.snappet.viewModels.ProfileViewModel
import com.example.snappet.sign_In.UserData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.snappet.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController : NavHostController,
                  userData: UserData?,
    //queremos ter um lambeda quando ele fizer sign out
    //ele recebe esta função como paramentro de entrada
                  onSignOut: () -> Unit
                          ) {
    val userDataState by profileViewModel.userData.observeAsState()
    // Check the current value of userDataState whenever it changes
    //println("userDataState: $userDataState")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "", modifier = Modifier.padding(paddingValues = paddingValues))
        ProfileScreenComposable(userDataState, userData, onSignOut,navController)

        //Text(text = "Hello")
    }

}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//para este Screen não precisamos de um view model pois ele não
// contém nenhum estado nem nada que "mude" ao longo do tempo
//só mostra user data estática
fun ProfileScreenComposable(
    final : UserData?,
    userData: UserData?,
    //queremos ter um lambeda quando ele fizer sign out
    //ele recebe esta função como paramentro de entrada
    onSignOut: () -> Unit,
    navController: NavController,
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Edit Profile",
                color = Color.White, // Change to Color.White for white text
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

                if (final != null) {
                    Text(
                        //mostra os pontos
                        text = "SnapPoints: "+final.snaPoints,
                        //o texto vai ser centrado
                        textAlign = TextAlign.Center,
                        //tamanho do texto
                        fontSize = 20.sp,
                        //por negrito no texto
                        fontWeight = FontWeight.SemiBold
                    )
                }
                //espacinho depois do username
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // User profile picture
                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Buttons (Update and Logout)
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp),

                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { /* Ação do botão Update */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp) // Adjust the spacing between buttons
                    ) {
                        Text(text = "Update")
                    }

                    Button(
                        onClick = onSignOut,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Logout")
                    }
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
                onClick = {
                    navController.navigate(route = Screens.Leaderboard.route)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 200.dp,
                        y = 680.dp
                    )
                    .height(50.dp)
                    .width(170.dp)

            )
            {
                Text(text = "Leaderboard", style = TextStyle(fontSize = 20.sp))
            }
        }
}