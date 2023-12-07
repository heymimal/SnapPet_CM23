package com.example.snappet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {

    //@-23.5868031,-46.6847268
    //Av. Brig. Faria Lima, 3477 - 18º Andar - Itaim Bibi, São Paulo - SP, 04538-133, Brasil


    //@-23.5894588,-46.6732638
    //Av. República do Líbano, 1157 - Ibirapuera, São Paulo - SP, 04502-001, Brasil

    private val places = arrayListOf(
        Place("Google", LatLng(-23.5868031,-46.6847268), "Av. Brig. Faria Lima, 3477 - 18º Andar - Itaim Bibi, São Paulo - SP", 4.8f),
        Place("Parque", LatLng(-23.5894588,-46.6732638), "Av. República do Líbano, 1157 - Ibirapuera, São Paulo - SP", 4.9f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        //mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync{googleMap ->
            addMarkers(googleMap)
        }
    }

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach{place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.address)
                    .position(place.latLng)
            )
        }
    }
}

data class Place(
    val name: String,
    val latLng: LatLng,
    val address: String,
    val rating : Float
)