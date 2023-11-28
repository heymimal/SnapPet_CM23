package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 800.dp)
                .background(color = Color.White))
        NavigationBar(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 737.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 63.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 1.dp)
                        .requiredWidth(width = 360.dp)
                        .requiredHeight(height = 62.dp)
                        .background(color = Color(0xffe99517).copy(alpha = 0.77f)))
                Image(
                    painter = painterResource(id = R.drawable.gameicon),
                    contentDescription = "game",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 234.dp,
                            y = 13.dp)
                        .requiredWidth(width = 38.dp)
                        .requiredHeight(height = 39.dp))
                Image(
                    painter = painterResource(id = R.drawable.homeicon),
                    contentDescription = "home",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 9.dp,
                            y = 13.dp)
                        .requiredWidth(width = 50.dp)
                        .requiredHeight(height = 42.dp))
                Image(
                    painter = painterResource(id = R.drawable.mapicon),
                    contentDescription = "map",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 85.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.profileicon),
                    contentDescription = "profile",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 298.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.cameraicon),
                    contentDescription = "Camera",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 161.dp,
                            y = 0.dp)
                        .requiredWidth(width = 47.dp)
                        .requiredHeight(height = 55.dp))
            }
        }
        Text(
            text = "Edit Profile",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.dp,
                    y = 29.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 31.dp,
                    y = 333.dp)
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
                    .offset(x = 0.dp,
                        y = 35.dp)
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
                        .offset(x = 14.dp,
                            y = 20.dp))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 31.dp,
                    y = 460.dp)
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
                    .offset(x = 0.dp,
                        y = 35.dp)
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
                        .offset(x = 14.dp,
                            y = 20.dp))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 246.dp,
                    y = 640.dp)
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
                    .offset(x = 20.dp,
                        y = 14.dp))
        }
        Image(
            painter = painterResource(id = R.drawable.profilepic),
            contentDescription = "image 2",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 25.dp,
                    y = 170.dp)
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
                .offset(x = 31.dp,
                    y = 120.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 214.dp,
                    y = 243.dp)
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
                    .offset(x = 7.dp,
                        y = 14.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 640.dp)
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
                    .offset(x = 19.dp,
                        y = 15.dp))
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun EditProfilePreview() {
    EditProfile(Modifier)
}