package com.example.snappet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.snappet.sign_In.UserData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel

class LoginStreakViewModel : ViewModel() {
    private val _loginStreakData = MutableLiveData<Int?>()
    private val _snaPointsData = MutableLiveData<String?>()

    val loginStreakData: LiveData<Int?> = _loginStreakData
    val snaPointsData: LiveData<String?> = _snaPointsData

    fun fetchData(userId: String) {
        val reference = Firebase.database.getReference("Users (Quim)").child(userId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val loginStreak = dataSnapshot.child("LoginStreak").getValue(Int::class.java)
                    val snaPoints = dataSnapshot.child("snaPoints").getValue(String::class.java)

                    loginStreak?.let {
                        _loginStreakData.value = it
                    }

                    snaPoints?.let {
                        _snaPointsData.value = it
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}