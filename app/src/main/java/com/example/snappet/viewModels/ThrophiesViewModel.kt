package com.example.snappet.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel
import com.example.snappet.data.DailyMission
import com.example.snappet.data.MonthlyMission

class ThrophiesViewModel : ViewModel() {
    private val _dailyMissionsData = MutableLiveData<List<DailyMission>>()
    val dailyMissionsData: LiveData<List<DailyMission>> get() = _dailyMissionsData

    private val _monthlyMissionsData = MutableLiveData<List<DailyMission>>()
    val monthlyMissionsData: LiveData<List<DailyMission>> get() = _monthlyMissionsData

    // Fetch daily missions
    fun fetchDailyMissions(userId: String) {
        fetchMissions(userId, "DailyMissions", _dailyMissionsData)
    }

    // Fetch monthly missions
    fun fetchMonthlyMissions(userId: String) {
        fetchMissions(userId, "MonthlyMissions", _monthlyMissionsData)
    }

    // Generic function to fetch missions
    private fun fetchMissions(userId: String, node: String, liveData: MutableLiveData<List<DailyMission>>) {
        val reference = Firebase.database.getReference("Users (Quim)").child(userId).child(node)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val missions = dataSnapshot.children
                    .mapNotNull { missionSnapshot ->
                        missionSnapshot.getValue(DailyMission::class.java)
                    }
                liveData.value = missions
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}