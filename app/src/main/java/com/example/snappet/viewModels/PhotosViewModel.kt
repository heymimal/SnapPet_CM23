package com.example.snappet.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snappet.data.Photo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PhotosViewModel : ViewModel() {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>> get() = _photos

    private val _photo = MutableLiveData<Photo>()
    val photo : LiveData<Photo> get() = _photo

    fun fetchAllPhotos(){
        val databaseReference: DatabaseReference = Firebase.database.reference.child("imagesTest")
            .child("allImages")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val photos = mutableListOf<Photo>()
                for (childSnapshot in snapshot.children) {
                    val imageUrl = childSnapshot.child("imageUrl").getValue(String::class.java)
                    val animalType = childSnapshot.child("animal").getValue(String::class.java)
                    val contextPhoto = childSnapshot.child("context").getValue(String::class.java)
                    val description = childSnapshot.child("description").getValue(String::class.java)
                    val id = childSnapshot.child("id").getValue(String::class.java)
                    val downloadUrl = childSnapshot.child("downloadUrl").getValue(String::class.java)
                    val sender = childSnapshot.child("sender").getValue(String::class.java)
                    val latitude = childSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude  = childSnapshot.child("longitude").getValue(Double::class.java)
                    val likes  = childSnapshot.child("likes").getValue(Int::class.java)
                    val senderName = childSnapshot.child("senderName").getValue(String::class.java)

                    imageUrl?.let {
                        val photo = Photo(
                            imageUri = Uri.parse(it),
                            animalType = animalType ?: "",
                            contextPhoto = contextPhoto ?: "",
                            description = description ?: "",
                            id = id ?: "",
                            downloadUrl = downloadUrl ?: "",
                            sender = sender ?: "",
                            latitude = latitude ?: 0.0,
                            longitude = longitude ?: 0.0,
                            likes = likes ?: 0,
                            senderName = senderName ?: ""
                        )
                        photos.add(photo)
                    }
                }
               _photos.value = photos.reversed()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}