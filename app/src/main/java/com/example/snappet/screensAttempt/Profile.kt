package com.example.snappet.screensAttempt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
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
import com.example.snappet.R


@Composable
fun Profiler(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        NavigationBar(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 400.dp)
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
                    contentDescription = "game icon",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 234.dp,
                            y = 13.dp)
                        .requiredWidth(width = 38.dp)
                        .requiredHeight(height = 39.dp))
                Image(
                    painter = painterResource(id = R.drawable.homeicon),
                    contentDescription = "home icon",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 9.dp,
                            y = 13.dp)
                        .requiredWidth(width = 50.dp)
                        .requiredHeight(height = 42.dp))
                Image(
                    painter = painterResource(id = R.drawable.mapicon),
                    contentDescription = "map icon",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 85.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.profileicon),
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 298.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.cameraicon),
                    contentDescription = "Camera icon",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 161.dp,
                            y = 0.dp)
                        .requiredWidth(width = 47.dp)
                        .requiredHeight(height = 55.dp))
            }
        }
        Text(
            text = "Olá, Utilizador!",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 29.dp,
                    y = 33.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 27.dp,
                    y = 330.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
                modifier = Modifier
                    .requiredWidth(width = 308.dp)
                    .requiredHeight(height = 61.dp)){ }
            Text(
                text = "Edit Profile",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 19.dp,
                        y = 15.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 419.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
                modifier = Modifier
                    .requiredWidth(width = 308.dp)
                    .requiredHeight(height = 61.dp)){ }
            Text(
                text = "Settings",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 19.dp,
                        y = 15.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 508.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
                modifier = Modifier
                    .requiredWidth(width = 308.dp)
                    .requiredHeight(height = 61.dp)){ }
            Text(
                text = "Log out",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 19.dp,
                        y = 15.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 206.dp,
                    y = 97.dp)
                .requiredWidth(width = 82.dp)
                .requiredHeight(height = 40.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.trophyicon),
                contentDescription = "Trophy icon",
                modifier = Modifier
                    .requiredWidth(width = 45.dp)
                    .requiredHeight(height = 40.dp))
            Text(
                text = "25",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 51.dp,
                        y = 5.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 161.dp,
                    y = 147.dp)
                .requiredWidth(width = 187.dp)
                .requiredHeight(height = 63.dp)
        ) {
            Text(
                text = "Lv.25",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 111.dp,
                        y = 0.dp))
            Text(
                text = "Lv.24",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 13.dp,
                        y = 0.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 30.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 34.dp,
                    y = 210.dp)
                .requiredWidth(width = 203.dp)
                .requiredHeight(height = 41.dp)
        ) {
            Text(
                text = "25 SnapPets",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 53.dp,
                        y = 5.dp))
            Image(
                painter = painterResource(id = R.drawable.cameraicon),
                contentDescription = "Camera Icon",
                modifier = Modifier
                    .requiredSize(size = 41.dp))
        }
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "profile_picture",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 34.dp,
                    y = 94.dp)
                .requiredWidth(width = 124.dp)
                .requiredHeight(height = 107.dp)
                .clip(shape = CircleShape))
    }
}

@Preview
@Composable
private fun ProfilePreview() {
    Profiler(Modifier)
}