package com.example.snappet.data

import kotlinx.serialization.Serializable


@Serializable
data class GeoPoint (
    val animalType : String = "",
    var radius : Double = 1500.0,
    var latitude: Double = 190.0,
    var longitude: Double = 190.0,
)