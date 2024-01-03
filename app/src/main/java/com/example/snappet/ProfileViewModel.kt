package com.example.snappet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.snappet.sign_In.UserData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {
    //private val _userData = MutableLiveData<UserData?>()
    //val userData: LiveData<UserData?> = _userData

    private val _userData = MutableLiveData<UserData?>(
        UserData(
            userId = "default_user_id",
            username = "default_username",
            profilePictureUrl = "default_url"
        )
    )
    val userData: LiveData<UserData?> = _userData

    fun fetchUserData(userId: String) {
        //println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA:$userId")
        val reference = Firebase.database.getReference("Users (Quim)").child(userId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val points = dataSnapshot.child("snaPoints").getValue(String::class.java)
                    points?.let {
                        println("Updating LiveData with points: $it")
                        _userData.value = _userData.value?.copy(snaPoints = it)
                    }
                    //println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB:$points")

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}