package com.example.snappet.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel

data class LeaderboardUserData(
    val userId: String,
    val username: String?,
    val snaPoints: String?
)

class LeaderboardViewModel : ViewModel() {
    private val _leaderboardData = MutableLiveData<List<LeaderboardUserData>>()
    val leaderboardData: LiveData<List<LeaderboardUserData>> get() = _leaderboardData

    fun fetchLeaderboardData() {
        val reference = Firebase.database.getReference("Users")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<LeaderboardUserData>()

                dataSnapshot.children.forEach { userSnapshot ->
                    val userId = userSnapshot.key
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val snaPoints = userSnapshot.child("snaPoints").getValue(String::class.java)

                    if (userId != null && username != null && snaPoints != null) {
                        users.add(LeaderboardUserData(userId, username, snaPoints))
                    }
                }

                _leaderboardData.value = users.sortedByDescending { it.snaPoints?.toIntOrNull() ?: 0 }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}