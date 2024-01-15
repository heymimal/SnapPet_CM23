package com.example.snappet.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.snappet.viewModels.LeaderboardUserData
import com.example.snappet.navigation.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun leaderboardNav(navController: NavHostController, leaderboardData: List<LeaderboardUserData>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Rank", fontWeight = FontWeight.Bold)
                Text(text = "Username", fontWeight = FontWeight.Bold)
                Text(text = "Points", fontWeight = FontWeight.Bold)
            }

            // List with scrollbar
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                // Header item
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                itemsIndexed(leaderboardData) { index, user ->
                    // Display each user's data in the list
                    // You can customize this part based on your UI requirements
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${index + 1}")
                        Text(text = user.username ?: "N/A")
                        Text(text = user.snaPoints ?: "N/A")
                    }
                }
            }
        }
    }
}