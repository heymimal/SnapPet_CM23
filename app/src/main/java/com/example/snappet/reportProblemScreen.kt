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
import androidx.compose.material3.NavigationBar
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
fun ReportProblem(modifier: Modifier = Modifier) {
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
            text = "Report Problem",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.5.dp,
                    y = 26.dp))
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
                    .offset(x = 18.dp,
                        y = 14.dp))
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
                text = "Report",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(x = 1.dp,
                        y = 14.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.dp,
                    y = 296.dp)
                .requiredWidth(width = 332.dp)
                .requiredHeight(height = 295.dp)
                .background(color = Color(0xffd9d9d9)))
        Text(
            text = "Report your problem here, we\nwill contact you as soon as\npossible.",
            color = Color(0xff707070),
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 34.dp,
                    y = 326.dp))
        Image(
            painter = painterResource(id = R.drawable.problem),
            contentDescription = "image 12",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.5.dp,
                    y = 90.dp)
                .requiredWidth(width = 177.dp)
                .requiredHeight(height = 173.dp))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun ReportProblemPreview() {
    ReportProblem(Modifier)
}