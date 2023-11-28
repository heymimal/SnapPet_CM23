package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.unit.sp

@Composable
fun Trophies(modifier: Modifier = Modifier) {
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
                        y = 737.dp)
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
                    painter = painterResource(id = R.drawable.trophyicon),
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
                    painter = painterResource(id = R.drawable.map_icon),
                    contentDescription = "map",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 85.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = "profile",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 298.dp,
                            y = 7.dp)
                        .requiredSize(size = 50.dp))
                Image(
                    painter = painterResource(id = R.drawable.dslr_camera),
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
            text = "Missions and Trophies",
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
                .offset(x = 31.dp,
                    y = 126.dp)
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 43.dp)
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.5.dp,
                        y = 0.dp))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.dp,
                        y = 89.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 29.dp,
                    y = 298.dp)
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 43.dp)
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 cat photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.dp,
                        y = 0.dp))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.dp,
                        y = 89.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 36.dp,
                    y = 470.dp)
                .requiredWidth(width = 297.dp)
                .requiredHeight(height = 119.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.progress_bar_icon),
                contentDescription = "image 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 43.dp)
                    .requiredWidth(width = 297.dp)
                    .requiredHeight(height = 33.dp))
            Text(
                text = "Take 10 dog photos!",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.dp,
                        y = 0.dp))
            Text(
                text = "7/10",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 0.dp,
                        y = 89.dp))
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun TrophiesPreview() {
    Trophies(Modifier)
}