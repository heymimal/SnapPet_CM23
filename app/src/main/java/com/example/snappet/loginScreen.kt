package com.example.snappet

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Login(modifier: Modifier = Modifier) {
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

        OptionsButton(text = "Sign In With Google", imageId = R.drawable.google_icon)
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

@Preview
@Composable
private fun LoginPreview() {
    Login(Modifier)
}


/*
Box(
            modifier = Modifier
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 800.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 800.dp)
                    .background(color = Color.White))

        }
        Text(
            text = "Login",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = (-18.5).dp,
                    y = 109.dp
                ))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 41.dp,
                    y = 451.dp
                )
                .requiredWidth(width = 277.dp)
                .requiredHeight(height = 64.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 277.dp)
                    .requiredHeight(height = 64.dp)
                    .background(color = Color(0xffd9d9d9)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 13.dp,
                        y = 13.dp
                    )
                    .requiredWidth(width = 243.dp)
                    .requiredHeight(height = 38.dp)
            ) {
                Text(
                    text = "Sign in with Google",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 20.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 56.dp,
                            y = 7.dp
                        ))
                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "image 3",
                    modifier = Modifier
                        .requiredSize(size = 38.dp))
            }
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 41.dp,
                    y = 263.dp
                )
                .requiredWidth(width = 277.dp)
                .requiredHeight(height = 64.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 277.dp)
                    .requiredHeight(height = 64.dp)
                    .background(color = Color(0xffe20b0b)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 10.dp,
                        y = 7.dp
                    )
                    .requiredWidth(width = 237.dp)
                    .requiredHeight(height = 50.dp)
            ) {
                Text(
                    text = "Sign in with email",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 20.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 68.dp,
                            y = 13.dp
                        ))
                Image(
                    painter = painterResource(id = R.drawable.email_icon),
                    contentDescription = "image 4",
                    modifier = Modifier
                        .requiredSize(size = 50.dp))
            }
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
            Box(
                modifier = Modifier
                    .requiredWidth(width = 84.dp)
                    .requiredHeight(height = 52.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Color(0xffe2590b)))
            Text(
                text = "Back",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 19.dp,
                        y = 14.dp
                    ))
        }
 */