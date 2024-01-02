package com.example.snappet.data

import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    var type: String = "",
    var id: String = "",
    var description:String  = "",
    var localSrc: String = "",
    var remoteSrc: String = "",
    var location: String = "",
    // to change
    )