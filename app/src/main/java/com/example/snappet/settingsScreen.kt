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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Settings(modifier: Modifier = Modifier) {
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
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 738.dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 62.dp)
                .background(color = Color(0xffe99517).copy(alpha = 0.77f)))
        Image(
            painter = painterResource(id = R.drawable.gameicon),
            contentDescription = "game",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 234.dp,
                    y = 750.dp)
                .requiredWidth(width = 38.dp)
                .requiredHeight(height = 39.dp))
        Image(
            painter = painterResource(id = R.drawable.homeicon),
            contentDescription = "home",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 9.dp,
                    y = 750.dp)
                .requiredWidth(width = 50.dp)
                .requiredHeight(height = 42.dp))
        Image(
            painter = painterResource(id = R.drawable.map_icon),
            contentDescription = "map",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 85.dp,
                    y = 744.dp)
                .requiredSize(size = 50.dp))
        Image(
            painter = painterResource(id = R.drawable.profile_icon),
            contentDescription = "profile",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 298.dp,
                    y = 744.dp)
                .requiredSize(size = 50.dp))
        Image(
            painter = painterResource(id = R.drawable.dslr_camera),
            contentDescription = "Camera",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 161.dp,
                    y = 737.dp)
                .requiredWidth(width = 47.dp)
                .requiredHeight(height = 55.dp))
        Text(
            text = "Settings",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = 0.dp,
                    y = 29.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 28.dp,
                    y = 386.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Report Problem",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 45.dp,
                    y = 402.dp))
        Image(
            painter = painterResource(id = R.drawable.report_problem_icon),
            contentDescription = "image 10",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(x = 275.dp,
                    y = 16.dp)
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 28.dp,
                    y = 266.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Customer Support",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 44.dp,
                    y = 281.dp))
        Image(
            painter = painterResource(id = R.drawable.customer_support_icon),
            contentDescription = "image 9",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(x = 279.dp,
                    y = (-104).dp)
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffd9d9d9)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 146.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)){ }
        Text(
            text = "Change Language",
            color = Color.Black,
            style = TextStyle(
                fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 44.dp,
                    y = 161.dp))
        Image(
            painter = painterResource(id = R.drawable.language_icon),
            contentDescription = "image 8",
            modifier = Modifier
                .align(alignment = Alignment.CenterStart)
                .offset(x = 275.dp,
                    y = (-224).dp)
                .requiredSize(size = 50.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 640.dp)
                .requiredWidth(width = 84.dp)
                .requiredHeight(height = 52.dp)){ }
        Text(
            text = "Back",
            color = Color.Black,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 43.dp,
                    y = 654.dp))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun SettingsPreview() {
    Settings(Modifier)
}