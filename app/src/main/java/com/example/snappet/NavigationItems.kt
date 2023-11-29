package com.example.snappet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource

data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    //val iconic :ImageVector = vectorResource(R.drawable.baseline_map_24)
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                //route = Screens.Home.route
            ),
            BottomNavigationItem(
                label = "Map",
                icon = Icons.Filled.LocationOn
                //route = Screens.Search.route
            ),
            BottomNavigationItem(
                label = "Camera",
                icon = Icons.Filled.Call
            ),
            BottomNavigationItem(
                label = "Trophy",
                icon = Icons.Filled.Notifications
            ),
            BottomNavigationItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                //route = Screens.Profile.route
            )
        )
    }
}