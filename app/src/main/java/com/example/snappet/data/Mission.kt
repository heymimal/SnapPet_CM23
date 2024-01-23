package com.example.snappet.data

data class Mission(
    val missionType: String = "",
    val goal: Int = 0,
    var userProgress: Int = 0,
    val points: Int = 0,
    val missionDescription: String = "",
    val createdDate: String = "",
    val completed: Boolean = false
)