package com.example.snappet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 738.dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 62.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 62.dp)
                    .background(color = Color(0xffe99517).copy(alpha = 0.77f)))
        }
        Text(
            text = "Register",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.5.dp,
                    y = 109.dp))
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
                    .requiredHeight(height = 52.dp)){

            }
            Text(
                text = "Next",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 19.dp,
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
                        y = 14.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 46.dp,
                    y = 357.dp)
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
                text = "Username",
                color = Color(0xff707070),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 14.dp,
                        y = 20.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 46.dp,
                    y = 465.dp)
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
                text = "Password",
                color = Color(0xff707070),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 14.dp,
                        y = 20.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 46.dp,
                    y = 249.dp)
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
                text = "Email",
                color = Color(0xff707070),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 14.dp,
                        y = 20.dp))
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun RegisterPreview() {
    Register(Modifier)
}