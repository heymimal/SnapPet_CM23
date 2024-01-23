package com.example.snappet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.snappet.data.GeoPoint
import com.example.snappet.database.haversine
import com.google.android.gms.location.LocationServices
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val geofenceRef = FirebaseDatabase.getInstance().getReference("geofences")

    private var count = 2;
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate(){
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }


    //Only retrieves the closest
    fun retrieveGeopointsFromDatabase(location: Location, onGeofencesRetrieved: (List<GeoPoint>) -> Unit) {
        val lat = location.latitude
        val lon = location.longitude

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val geofences = mutableListOf<GeoPoint>()

                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        val animalType = snap.child("animalType").getValue(String::class.java)
                        val radius = snap.child("radius").getValue(Double::class.java)
                        val latitude = snap.child("latitude").getValue(Double::class.java)
                        val longitude = snap.child("longitude").getValue(Double::class.java)

                        if (animalType != null && radius != null && latitude != null && longitude != null) {
                            val geofenceData = GeoPoint(
                                animalType,
                                radius,
                                latitude,
                                longitude
                            )
                            geofences.add(geofenceData)
                        }
                    }
                    val sortedGeofences = geofences.sortedBy {
                        haversine(lat, lon, it.latitude, it.longitude)
                    }

                    // Take the top 3 closest geofences
                    val closestGeofences = sortedGeofences.take(3)

                    // Invoke the callback with the closest geofences
                    onGeofencesRetrieved(closestGeofences)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
            }
        }

        // Attach the ValueEventListener to geofenceRef
        geofenceRef.addListenerForSingleValueEvent(valueEventListener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        stopForeground(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        stopSelf()
    }
    // Function to create a notification channel
    private fun createNotificationChannel(channelId: String, name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun start() {
        Log.d(TAG, "STARTED")
        val notification = NotificationCompat.Builder(this,"location")
            .setContentTitle("Tracking location ... ")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
        createNotificationChannel("animal1","Animal Nearby!","not important")
        createNotificationChannel("animal2","Animal Nearby!","not important")
        createNotificationChannel("animal3","animal","not important")
        val animalNotification1 = NotificationCompat.Builder(this,"animal1")
        val animalNotification2 = NotificationCompat.Builder(this,"animal2")
        val animalNotification3 = NotificationCompat.Builder(this,"animal3")

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        locationClient.getLocationUpdates(10000L)
            .catch { e ->
                e.printStackTrace()
            }.onEach {
                location ->
                //Geofence at home: Same ideia regarding before, get the "geopoints" from db
                // and use them to compare distance, and if you are in the range, send a notification!
                retrieveGeopointsFromDatabase(location) { geofences ->
                    Log.d(TAG,"count: $count")
                    var c = 1;
                    for(geofence in geofences){
                        val distance = haversine(location.latitude,location.longitude,geofence.latitude,geofence.longitude)
                        if(c==1){
                            val animalNotif = animalNotification1.setContentText("A ${geofence.animalType} is close by!!\n${distance.toInt()} meters away...")
                                .setContentTitle("Animal nearby!")
                                .setSmallIcon(getIcon(geofence.animalType))
                                .setOngoing(true)
                                .setTimeoutAfter(TimeUnit.MINUTES.toMillis(1))
                            notificationManager.notify(2, animalNotif.build())
                        } else if(c==2){
                            val animalNotif = animalNotification2.setContentText("A ${geofence.animalType} is close by!!\n${distance.toInt()} meters away...")
                                .setContentTitle("Animal nearby!")
                                .setSmallIcon(getIcon(geofence.animalType))
                                .setOngoing(true)
                                .setTimeoutAfter(TimeUnit.MINUTES.toMillis(1))
                            notificationManager.notify(3, animalNotif.build())
                        } else if(c==3){
                            val animalNotif = animalNotification3.setContentText("A ${geofence.animalType} is close by!!\n${distance.toInt()} meters away...")
                                .setContentTitle("Animal nearby!")
                                .setSmallIcon(getIcon(geofence.animalType))
                                .setOngoing(true)
                                .setTimeoutAfter(TimeUnit.MINUTES.toMillis(1))
                            notificationManager.notify(4, animalNotif.build())
                        }
                        c++
                    }
                }

                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat,$long)"
                )
                notificationManager.notify(1,updatedNotification.build())
            }
            .launchIn(serviceScope)
        startForeground(1,notification.build())
    }
    private fun getIcon(animalType: String): Int {
        if(animalType == "Bee") return R.drawable.abelha_icon
        if(animalType == "Bird") return R.drawable.bird_icon
        if(animalType == "Butterfly") return R.drawable.borboleta_icon
        if(animalType == "Cat") return R.drawable.cat_icon
        if(animalType == "Chicken") return R.drawable.chicken_icon
        if(animalType == "Cow") return R.drawable.cow_icon
        if(animalType == "Dog") return R.drawable.dog_icon
        if(animalType == "Duck") return R.drawable.duck_icon
        if(animalType == "Gecko") return R.drawable.gecko_icon
        if(animalType == "Goat") return R.drawable.goat_icon
        if(animalType == "Horse") return R.drawable.horse_icon
        if(animalType == "Lizard") return R.drawable.gecko_icon
        if(animalType == "Pig") return R.drawable.pig_icon
        if(animalType == "Rabbit") return R.drawable.rabbit_icon
        if(animalType == "Peacock") return R.drawable.peacock_icon
        if(animalType == "Sheep") return R.drawable.sheep_icon
        return 10
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}