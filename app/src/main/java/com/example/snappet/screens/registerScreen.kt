package com.example.snappet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Register",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .offset(y = 29.dp)
        )

        Spacer(modifier = Modifier.height(150.dp))

        TextField(
            value = "",
            onValueChange = {/*TODO*/},
            label = {Text("Email")},
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(30.dp))

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
                    x = 246.dp,
                    y = 680.dp
                )
                .height(50.dp)
                .width(100.dp)

        )
        {
            Text(text = "Next", style = TextStyle(fontSize = 20.sp))
        }
    }
}

@Preview
@Composable
private fun RegisterPreview() {
    Register(Modifier)
}
