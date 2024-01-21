package com.example.snappet

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.snappet.data.Photo
import com.example.snappet.viewModels.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PinConfig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
//import com.google.maps.android.compose.theme.MapsComposeSampleTheme
import kotlinx.coroutines.launch

private const val TAG = "BasicMapActivity"

val singapore = LatLng(1.3588227, 103.8742114)
val singapore2 = LatLng(1.40, 103.77)
val singapore3 = LatLng(1.45, 103.77)
val singapore4 = LatLng(1.50, 103.77)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(singapore, 11f)


class TestMapActivity : ComponentActivity() {

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            var context = LocalContext.current
            var isMapLoaded by remember { mutableStateOf(false) }
            var locationPoint by remember { mutableStateOf<Location?>(null) }
            var photosViewModel by remember { mutableStateOf(PhotosViewModel()) }


            val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val locationPermission = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }
                    if (!permissionsGranted) {
                        // Logic when the permissions were not granted by the user
                    } else {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        mFusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                if (location != null) {
                                    Log.d(TAG, "Location ain't null: $location")
                                    locationPoint = location
                                    isMapLoaded = true
                                    photosViewModel.fetchAllPhotos()

                                } else {
                                    Log.d(TAG, "No location")
                                }
                            }
                    }
                })

            DisposableEffect(Unit) {
                locationPermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                onDispose {
                    // Clean up if needed
                }
            }
            val photosState by photosViewModel.photos.observeAsState()

            // Conditional rendering of the map based on location permission
            if (isMapLoaded) {

                var itemsCheck by remember { mutableStateOf(false) }

                val current = LatLng(locationPoint!!.latitude,locationPoint!!.longitude)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(current, 15f)
                }
                photosState?.let { photos ->
                    val items = remember { mutableStateListOf<MyPhotoCluster>()}
                    LaunchedEffect(Unit){
                        for(photo in photos){
                            val position = LatLng(photo.latitude,photo.longitude)
                            items.add(MyPhotoCluster(position,photo.animalType,photo.description,0f,photo))
                        }
                        itemsCheck = true
                    }
                    if(itemsCheck){
                        GoogleMapClustering(items = items, cameraPositionState = cameraPositionState )
                    }
                    /*GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                    ) {
                        Log.d(TAG, "Photos length ${photos.size}")
                        for (photo in photos) {
                            if(photo.latitude != 190.0 && photo.longitude != 190.0){
                                val position = LatLng(photo.latitude, photo.longitude)
                                Log.d(TAG, "Testing for photo with position $position")
                                Marker(
                                    state = MarkerState(position = position),
                                    title = photo.animalType,
                                    snippet = photo.description
                                )
                            }
                        }
                    }*/
                }
            } else {
                // Map is not loaded yet, you can show a loading indicator or handle it accordingly
                Text("Loading Map...") // CHANGE FOR SOMETHING BETTER !
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi ::class)
@Composable
fun GoogleMapClustering(items : List<MyPhotoCluster>, cameraPositionState: CameraPositionState){
    var touched by remember {
        mutableStateOf(false)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
      Clustering (
          items = items,
          onClusterClick = {
              Log.d(TAG, "Cluster clicked! $it")
              false
          },
          onClusterItemClick = {
              Log.d(TAG, "Cluster item clicked! $it")
              false
          },
          onClusterItemInfoWindowClick = {
              touched = true
              Log.d(TAG, "Cluster item info window clicked! $it")
          },
          clusterContent = { cluster ->
              CircleContent(
                  modifier = Modifier.size(40.dp),
                  text = "%,d".format(cluster.size),
                  color = Color.Blue,
              )
          },
          clusterItemContent = null
      )
    }
    if(touched)   Text("Testing")

}


@Composable
private fun CircleContent(
    color: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier,
        shape = CircleShape,
        color = color,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.White)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}


data class MyPhotoCluster(
    val itemPosition: LatLng,
    val itemTitle: String,
    val itemSnippet: String,
    val itemZIndex: Float,
    val photoInfo : Photo,
) : ClusterItem {
    override fun getPosition(): LatLng =
        itemPosition

    override fun getTitle(): String =
        itemTitle

    override fun getSnippet(): String =
        itemSnippet

    override fun getZIndex(): Float =
        itemZIndex

    fun getPhoto() : Photo = photoInfo
}
