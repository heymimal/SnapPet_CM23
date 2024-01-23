package com.example.snappet.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel
import com.example.snappet.data.Mission

class ThrophiesViewModel : ViewModel() {
    private val _MissionsData = MutableLiveData<List<Mission>>()
    val missionsData: LiveData<List<Mission>> get() = _MissionsData

    private val _monthlyMissionsData = MutableLiveData<List<Mission>>()
    val monthlyMissionsData: LiveData<List<Mission>> get() = _monthlyMissionsData

    private val _fullTimeMissionsData = MutableLiveData<List<Mission>>()
    val fullTimeMissionsData: LiveData<List<Mission>> get() = _fullTimeMissionsData

    // Fetch daily missions
    fun fetchDailyMissions(userId: String) {
        fetchMissions(userId, "DailyMissions", _MissionsData)
    }

    // Fetch monthly missions
    fun fetchMonthlyMissions(userId: String) {
        fetchMissions(userId, "MonthlyMissions", _monthlyMissionsData)
    }

    // Fetch full-time missions
    fun fetchFullTimeMissions(userId: String) {
        fetchMissions(userId, "FullTimeMissions", _fullTimeMissionsData)
    }

    // Generic function to fetch missions
    private fun fetchMissions(userId: String, node: String, liveData: MutableLiveData<List<Mission>>) {
        val reference = Firebase.database.getReference("Users").child(userId).child(node)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val missions = dataSnapshot.children
                    .mapNotNull { missionSnapshot ->
                        missionSnapshot.getValue(Mission::class.java)
                    }
                liveData.value = missions
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}