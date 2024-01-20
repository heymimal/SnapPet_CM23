// Copyright 2023 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
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
                                    Log.d(TAG,"longitude: " + location.longitude + " latitude: " + location.latitude)
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

            //photos = tempP()

            // Conditional rendering of the map based on location permission
            if (isMapLoaded) {
                // Map is loaded, render the map composable

                val singapore = LatLng(1.35, 103.87)
                val current = LatLng(locationPoint!!.latitude,locationPoint!!.longitude)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(current, 15f)
                }
                /*GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,

                ) {
                    Log.d(TAG,"Photos lenght ${photos.size}")
                    for(photo in photos){
                        val position = LatLng(photo.latitude,photo.longitude)
                        Log.d(TAG, "testing for photo with position $position")
                        Marker(
                            state = MarkerState(position = position),
                            title = photo.animalType,
                            snippet = photo.description
                        )
                    }
                }*/
                photosState?.let { photos ->
                    GoogleMap(
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
                    }
                }
            } else {
                // Map is not loaded yet, you can show a loading indicator or handle it accordingly
                Text("Loading Map...")
            }
        }
    }
}

/*class TestMapActivity : ComponentActivity() {

    lateinit var mFusedLocationClient : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var context = LocalContext.current
            var isMapLoaded by remember { mutableStateOf(false) }
            var locationPoint by remember { mutableStateOf<Location?>(null)}

            val locationPermissionsAlreadyGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

            val locationPermission = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    val permissionsGranted = permissions.values.reduce { acc, isPermissionGranted ->
                        acc && isPermissionGranted
                    }
                    if (!permissionsGranted) {
                        //Logic when the permissions were not granted by the user
                    } else {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                        mFusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->
                                if(location != null){
                                    Log.d(TAG, "Location ain't null: $location")
                                    locationPoint= location
                                    //temp(context)
                                } else {
                                    Log.d(TAG,"No location")
                                }
                            }
                    }
                })
            DisposableEffect(Unit) {
                //permissionLauncher.launch(Manifest.permission.CAMERA)
                locationPermission.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
                onDispose {
                    // Clean up if needed
                }
            }
            if(locationPoint!=null){
                temp(context = context)
            }*/
            /*Box(Modifier.fillMaxSize()) {
                GoogleMapView(
                    cameraPositionState = cameraPositionState,
                    onMapLoaded = {
                        isMapLoaded = true
                    },
                )
                if (!isMapLoaded) {
                    AnimatedVisibility(
                        modifier = Modifier
                            .matchParentSize(),
                        visible = !isMapLoaded,
                        enter = EnterTransition.None,
                        exit = fadeOut()
                    ) {
                        /*CircularProgressIndicator(
                            modifier = Modifier
                                .background(MaterialTheme.colors.background)
                                .wrapContentSize()
                        )*/
                    }
                }
            }
        }
    }
}*/

@Composable
fun temp(context : Context){
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }
    Button(
        onClick = {
            (context as? ComponentActivity)?.finish()
        }
    ) {
        Text("CLick");
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val singaporeState = rememberMarkerState(position = singapore)
    val singapore2State = rememberMarkerState(position = singapore2)
    val singapore3State = rememberMarkerState(position = singapore3)
    val singapore4State = rememberMarkerState(position = singapore4)
    var circleCenter by remember { mutableStateOf(singapore) }
    if (singaporeState.dragState == DragState.END) {
        circleCenter = singaporeState.position
    }

    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var shouldAnimateZoom by remember { mutableStateOf(true) }
    var ticker by remember { mutableIntStateOf(0) }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var mapVisible by remember { mutableStateOf(true) }

    if (mapVisible) {
        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapLoaded = onMapLoaded,
            onPOIClick = {
                Log.d(TAG, "POI clicked: ${it.name}")
            }
        ) {
            // Drawing on the map is accomplished with a child-based API
            val markerClick: (Marker) -> Boolean = {
                Log.d(TAG, "${it.title} was clicked")
                cameraPositionState.projection?.let { projection ->
                    Log.d(TAG, "The current projection is: $projection")
                }
                false
            }
            MarkerInfoWindowContent(
                state = singaporeState,
                title = "Zoom in has been tapped $ticker times.",
                onClick = markerClick,
                draggable = true,
            ) {
                Text(it.title ?: "Title", color = Color.Red)
            }
            MarkerInfoWindowContent(
                state = singapore2State,
                title = "Marker with custom info window.\nZoom in has been tapped $ticker times.",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                onClick = markerClick,
            ) {
                Text(it.title ?: "Title", color = Color.Blue)
            }

            /*Marker(
                state = singapore3State,
                title = "Marker in Singapore",
                onClick = markerClick
            )*/
            MarkerComposable(
                title = "Marker Composable",
                keys = arrayOf("singapore4"),
                state = singapore4State,
                onClick = markerClick,
            ) {
                Box(
                    modifier = Modifier
                        .width(88.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Compose Marker",
                        textAlign = TextAlign.Center,
                    )
                }
            }
            /*Circle(
                center = circleCenter,
                fillColor = MaterialTheme.colors.secondary,
                strokeColor = MaterialTheme.colors.secondaryVariant,
                radius = 1000.0,
            )*/
            content()
        }

    }
    Column {
        MapTypeControls(onMapTypeClick = {
            Log.d("GoogleMap", "Selected map type $it")
            mapProperties = mapProperties.copy(mapType = it)
        })
        Row {
            MapButton(
                text = "Reset Map",
                onClick = {
                    mapProperties = mapProperties.copy(mapType = MapType.NORMAL)
                    cameraPositionState.position = defaultCameraPosition
                    singaporeState.position = singapore
                    singaporeState.hideInfoWindow()
                }
            )
            MapButton(
                text = "Toggle Map",
                onClick = { mapVisible = !mapVisible },
                modifier = Modifier
                    .testTag("toggleMapVisibility")
            )
        }
        val coroutineScope = rememberCoroutineScope()
        ZoomControls(
            shouldAnimateZoom,
            uiSettings.zoomControlsEnabled,
            onZoomOut = {
                if (shouldAnimateZoom) {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                    }
                } else {
                    cameraPositionState.move(CameraUpdateFactory.zoomOut())
                }
            },
            onZoomIn = {
                if (shouldAnimateZoom) {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                    }
                } else {
                    cameraPositionState.move(CameraUpdateFactory.zoomIn())
                }
                ticker++
            },
            onCameraAnimationCheckedChange = {
                shouldAnimateZoom = it
            },
            onZoomControlsCheckedChange = {
                uiSettings = uiSettings.copy(zoomControlsEnabled = it)
            }
        )
        DebugView(cameraPositionState, singaporeState)
    }
}

@Composable
private fun MapTypeControls(
    onMapTypeClick: (MapType) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(state = ScrollState(0)),
        horizontalArrangement = Arrangement.Center
    ) {
        MapType.values().forEach {
            MapTypeButton(type = it) { onMapTypeClick(it) }
        }
    }
}

@Composable
private fun MapTypeButton(type: MapType, onClick: () -> Unit) =
    MapButton(text = type.toString(), onClick = onClick)

@Composable
private fun ZoomControls(
    isCameraAnimationChecked: Boolean,
    isZoomControlsEnabledChecked: Boolean,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit,
    onCameraAnimationCheckedChange: (Boolean) -> Unit,
    onZoomControlsCheckedChange: (Boolean) -> Unit,
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        MapButton("-", onClick = { onZoomOut() })
        MapButton("+", onClick = { onZoomIn() })
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = "Camera Animations On?")
            Switch(
                isCameraAnimationChecked,
                onCheckedChange = onCameraAnimationCheckedChange,
                modifier = Modifier.testTag("cameraAnimations"),
            )
            Text(text = "Zoom Controls On?")
            Switch(
                isZoomControlsEnabledChecked,
                onCheckedChange = onZoomControlsCheckedChange
            )
        }
    }
}

@Composable
private fun MapButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier.padding(4.dp),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
private fun DebugView(
    cameraPositionState: CameraPositionState,
    markerState: MarkerState
) {
    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        val moving =
            if (cameraPositionState.isMoving) "moving" else "not moving"
        Text(text = "Camera is $moving")
        Text(text = "Camera position is ${cameraPositionState.position}")
        Spacer(modifier = Modifier.height(4.dp))
        val dragging =
            if (markerState.dragState == DragState.DRAG) "dragging" else "not dragging"
        Text(text = "Marker is $dragging")
        Text(text = "Marker position is ${markerState.position}")
    }
}

