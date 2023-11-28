package com.example.snappet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PhotoForms(modifier: Modifier = Modifier) {
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
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 26.dp,
                    y = 295.dp)
                .requiredWidth(width = 308.dp)
                .requiredHeight(height = 61.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 308.dp)
                    .requiredHeight(height = 61.dp)
                    .background(color = Color(0xffd9d9d9)))
            Text(
                text = "Animal",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 19.dp,
                        y = 15.dp))
            Image(
                painter = painterResource(id = R.drawable.drop),
                contentDescription = "Polygon 1",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 273.dp,
                        y = 19.dp)
                    .requiredWidth(width = 23.dp)
                    .requiredHeight(height = 26.dp)
                    .rotate(degrees = 180f))
        }
        Image(
            painter = painterResource(id = R.drawable.imagemforms),
            contentDescription = "image 13",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(x = (-0.5).dp,
                    y = 23.dp)
                .requiredWidth(width = 325.dp)
                .requiredHeight(height = 228.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 28.dp,
                    y = 638.dp)
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
                .offset(x = 250.dp,
                    y = 638.dp)
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
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 28.dp,
                    y = 385.dp)
                .requiredWidth(width = 319.dp)
                .requiredHeight(height = 121.dp)
        ) {
            Text(
                text = "Context",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 7.dp,
                        y = 46.dp)
                    .requiredWidth(width = 217.dp)
                    .requiredHeight(height = 30.dp)
            ) {
                Text(
                    text = "Entertainment",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 50.dp,
                            y = 0.dp))
                val checkedState = remember { mutableStateOf(true) }
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 3.dp))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 7.dp,
                        y = 91.dp)
                    .requiredWidth(width = 312.dp)
                    .requiredHeight(height = 30.dp)
            ) {
                Text(
                    text = "Animal in need of help",
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 25.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 50.dp,
                            y = 0.dp))
                val checkedState = remember { mutableStateOf(true) }
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 5.dp))
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PhotoFormsPreview() {
    PhotoForms(Modifier)
}