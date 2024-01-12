package com.example.snappet

import android.net.Uri

data class CapturedPhoto(
    val imageUri: Uri,
    val animalType: String,
    val contextPhoto: String,
    val description: String
)
