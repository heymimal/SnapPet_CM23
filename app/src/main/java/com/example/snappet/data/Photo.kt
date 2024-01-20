package com.example.snappet.data

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val imageUri: Uri = Uri.EMPTY,
    var animalType : String = "",
    var contextPhoto: String = "",
    var description: String = "",
    var id: String = "",
    var latitude: Double,
    var longitude: Double
    //var description:String  = "",
    //var localSrc: String = "",
    //var remoteSrc: String = "",
    //var location: String = "",
    // to change
    )
