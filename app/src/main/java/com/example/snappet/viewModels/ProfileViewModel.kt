package com.example.snappet.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.snappet.data.Trophy
import com.example.snappet.sign_In.UserData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    private val _userData = MutableLiveData<UserData?>(
        UserData(
            userId = "default_user_id",
            username = "default_username",
            profilePictureUrl = "default_url"
        )
    )
    val userData: LiveData<UserData?> = _userData

    // Separate LiveData for Trophy
    private val _trophyData = MutableLiveData<Trophy>()
    val trophyData: LiveData<Trophy> = _trophyData

    fun fetchUserData(userId: String) {
        val reference = Firebase.database.getReference("Users").child(userId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Fetch Trophy data
                    val trophySnapshot = dataSnapshot.child("Trophy")
                    val trophyType = trophySnapshot.child("trophyType").getValue(String::class.java)
                    val trophyText = trophySnapshot.child("text").getValue(String::class.java)

                    trophyType?.let { type ->
                        trophyText?.let { text ->
                            // Update LiveData with the fetched Trophy data
                            _trophyData.postValue(Trophy(type, text))
                        }
                    }

                    val points = dataSnapshot.child("snaPoints").getValue(String::class.java)
                    points?.let {
                        println("Updating LiveData with points: $it")
                        _userData.value = _userData.value?.copy(snaPoints = it)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}