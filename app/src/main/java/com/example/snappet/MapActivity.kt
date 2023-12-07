package com.example.snappet

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() {


    //a ideia seria estes places serem armazenados numa BD. Quando alguém adicionasse uma foto nova
    //a localizacao era adicionada à BD, e a localização assinalada no mapa.
    private val places = arrayListOf(
        Place("Google", LatLng(-23.5868031,-46.6847268), "Av. Brig. Faria Lima, 3477 - 18º Andar - Itaim Bibi, São Paulo - SP", 4.8f, "outra info precisa"),
        Place("Parque", LatLng(-23.5894588,-46.6732638), "Av. República do Líbano, 1157 - Ibirapuera, São Paulo - SP", 4.9f, "outra info precisa"),
        Place("Faculdade de Ciências Lisboa", LatLng(38.7565253,-9.1579553), "Campo Grande 016, 1749-016 Lisboa", 4.9f, "outra info precisa")
        )

    companion object{
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync{map ->
            googleMap = map
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
            addMarkers(googleMap)

            // Request location permission if not granted
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Get the last known location
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        // Set initial camera position to the user's current location
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        val cameraPosition =
                            CameraPosition.Builder().target(userLatLng).zoom(15.0f).build()
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                        addMarker(userLatLng, "Your location", "You're here!")
                    }
                }
            } else {
                // Request location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }

        }
    }


    private fun addMarker(latLng: LatLng, title: String, snippet: String) {
        val marker = googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
                .icon(
                    //BitmapHelper.vectorToBitmap(this, R.drawable.smartphone_call, ContextCompat.getColor(this, R.color.black))
                    BitmapHelper.vectorToBitmap(this, R.drawable.smartphone_call, ContextCompat.getColor(this, R.color.black), 70, 70)
                )
        )
        marker?.tag = "YourLocationTag" // You can set a tag to identify this marker if needed
    }

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach{place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.address)
                    .position(place.latLng)
            )
            marker?.tag = place
        }
    }
}

data class Place(
    val name: String,
    val latLng: LatLng,
    val address: String,
    val rating : Float,
    val info: String
)