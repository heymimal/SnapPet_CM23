package com.example.snappet.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModel
import com.example.snappet.data.DailyMission

class ThrophiesViewModel : ViewModel() {
    private val _dailyMissionsData = MutableLiveData<List<DailyMission>>()
    val dailyMissionsData: LiveData<List<DailyMission>> get() = _dailyMissionsData

    fun fetchDailyMissions(userId: String) {
        val reference = Firebase.database.getReference("Users (Quim)").child(userId).child("DailyMissions")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val missions = dataSnapshot.children
                    .mapNotNull { missionSnapshot ->
                        val missionData = missionSnapshot.getValue(DailyMission::class.java)
                        missionData?.copy(
                            missionType = missionSnapshot.child("missionType").getValue(String::class.java) ?: missionData?.missionType ?: "",
                            goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: missionData?.goal ?: 0,
                            userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: missionData?.userProgress ?: 0,
                            points = missionSnapshot.child("points").getValue(Int::class.java) ?: missionData?.points ?: 0,
                            missionDescription = missionSnapshot.child("missionDescription").getValue(String::class.java) ?: missionData?.missionDescription ?: "",
                            completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: missionData?.completed ?: false,
                            createdDate = missionSnapshot.child("createdDate").getValue(String::class.java) ?: missionData?.createdDate ?: ""
                        )
                    }
                _dailyMissionsData.value = missions
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        })
    }
}