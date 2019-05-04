package com.acostadev.acostademo.repositories

import androidx.lifecycle.MutableLiveData
import com.acostadev.acostademo.models.TextProfile
import com.google.firebase.database.*
import java.lang.Exception

object FirebaseDbRepository {

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    fun getProfiles(liveData: MutableLiveData<List<TextProfile>>) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println("FirebaseDatabase => Error ${p0.message}")
                liveData.value = null
            }

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    if (p0.value != null) {
                        val profiles = (p0.value as HashMap<String, String>).map { TextProfile(it.key, (it.value as HashMap<String, String>)["words"]!!.split(",").toList()) }
                        liveData.value = profiles
                    }
                } catch (e: Exception) {

                }
            }
        })
    }
}