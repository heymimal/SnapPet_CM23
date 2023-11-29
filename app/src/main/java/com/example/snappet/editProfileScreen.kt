package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editProfileNavBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(currentDestination = currentDestination, navController =navController)
        }) {paddingValues ->
        Text(text = "SnapPet Menu", modifier = Modifier.padding(paddingValues = paddingValues))
        EditProfile()

        //Text(text = "Hello")
    }
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
}

@Preview
@Composable
private fun EditProfilePreview() {
    editProfileNavBar()
}



/*
Text(
            text = "Edit Profile",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 0.dp,
                    y = 29.dp
                ))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 31.dp,
                    y = 333.dp
                )
                .requiredWidth(width = 277.dp)
                .requiredHeight(height = 99.dp)
        ) {
            Text(
                text = "Username",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 0.dp,
                        y = 35.dp
                    )
                    .requiredWidth(width = 277.dp)
                    .requiredHeight(height = 64.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xffd9d9d9)),
                    modifier = Modifier
                        .requiredWidth(width = 277.dp)
                        .requiredHeight(height = 64.dp))
                Text(
                    text = "username",
                    color = Color(0xff707070),
                    style = TextStyle(
                        fontSize = 20.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 14.dp,
                            y = 20.dp
                        ))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 31.dp,
                    y = 460.dp
                )
                .requiredWidth(width = 277.dp)
                .requiredHeight(height = 99.dp)
        ) {
            Text(
                text = "Password",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 0.dp,
                        y = 35.dp
                    )
                    .requiredWidth(width = 277.dp)
                    .requiredHeight(height = 64.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xffd9d9d9)),
                    modifier = Modifier
                        .requiredWidth(width = 277.dp)
                        .requiredHeight(height = 64.dp))
                Text(
                    text = "password",
                    color = Color(0xff707070),
                    style = TextStyle(
                        fontSize = 20.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 14.dp,
                            y = 20.dp
                        ))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 246.dp,
                    y = 640.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                modifier = Modifier
                    .requiredWidth(width = 84.dp)
                    .requiredHeight(height = 52.dp)){ }
            Text(
                text = "Save",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 20.dp,
                        y = 14.dp
                    ))
        }
        Image(
            painter = painterResource(id = R.drawable.profilepic),
            contentDescription = "image 2",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 25.dp,
                    y = 170.dp
                )
                .requiredWidth(width = 145.dp)
                .requiredHeight(height = 125.dp)
                .clip(shape = RoundedCornerShape(50.dp)))
        Text(
            text = "Profile Image",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 31.dp,
                    y = 120.dp
                ))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 214.dp,
                    y = 243.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .requiredWidth(width = 84.dp)
                    .requiredHeight(height = 52.dp)){ }
            Text(
                text = "Upload",
                color = Color.White,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 7.dp,
                        y = 14.dp
                    ))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 26.dp,
                    y = 640.dp
                )
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
                modifier = Modifier
                    .requiredWidth(width = 84.dp)
                    .requiredHeight(height = 52.dp)){ }
            Text(
                text = "Back",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 19.dp,
                        y = 15.dp
                    ))
        }
 */