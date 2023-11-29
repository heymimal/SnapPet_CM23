package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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

@Composable
fun Login(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
    ) {
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
        }
        Text(
            text = "Login",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = (-18.5).dp,
                    y = 109.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 41.dp,
                    y = 451.dp)
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
                    .offset(x = 13.dp,
                        y = 13.dp)
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
                        .offset(x = 56.dp,
                            y = 7.dp))
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
                .offset(x = 41.dp,
                    y = 263.dp)
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
                    .offset(x = 10.dp,
                        y = 7.dp)
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
                        .offset(x = 68.dp,
                            y = 13.dp))
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
                .offset(x = 26.dp,
                    y = 640.dp)
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
                    .offset(x = 19.dp,
                        y = 14.dp))
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun LoginPreview() {
    Login(Modifier)
}