package com.example.snappet

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.snappet.data.Photo
import com.example.snappet.screens.PhotoDetailCard
import com.example.snappet.viewModels.PhotosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
//import com.google.maps.android.compose.theme.MapsComposeSampleTheme

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
                            if(photo.latitude != 190.0 && photo.longitude != 190.0){
                                val position = LatLng(photo.latitude,photo.longitude)
                                items.add(MyPhotoCluster(position,photo.animalType,photo.description,0f,photo))
                            }
                           }
                        itemsCheck = true
                    }
                    if(itemsCheck){
                        GoogleMapClustering(items = items, cameraPositionState = cameraPositionState )
                    }
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
    var photon = Photo(latitude = 190.0, longitude = 190.0)
    var clickSinglePoint by remember {
        mutableStateOf(false)
    }
    var clickOnCluster by remember {
        mutableStateOf(false)
    }

    var photo by remember {
        mutableStateOf(photon)
    }

    var photos by remember {
        mutableStateOf(emptyList<Photo>())
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
      Clustering (
          items = items,
          onClusterClick = {
              clickSinglePoint = false
              clickOnCluster = false
              Log.d(TAG,"This is the photo test:\n$photos")
              photos =  it.items.map { it.photoInfo }
              Log.d(TAG,"${it.items}")
              Log.d(TAG,"This is the photo test:\n$photos")
              Log.d(TAG, "Cluster clicked! $it")
              false
          },
          onClusterItemClick = {
              Log.d(TAG, "Cluster item clicked! $it")
              false
          },
          onClusterItemInfoWindowClick = {
              clickSinglePoint = false
              photo = it.photoInfo
              clickSinglePoint = true
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
    if(clickSinglePoint)   {
        Column {
            //Text("Clicked!")
            Log.d(TAG,"photo info: $photo")
            PhotoDetailCard(photo)
            Button(onClick = { clickSinglePoint = false}) {
                Text(text = "click me")
            }
        }
    }

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
