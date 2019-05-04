package com.acostadev.acostademo.main

import androidx.lifecycle.ViewModel
import com.acostadev.acostademo.models.TextProfile
import com.acostadev.acostademo.mutableLiveData
import com.acostadev.acostademo.repositories.FirebaseDbRepository
import com.acostadev.acostademo.repositories.MlKitRepository
import com.google.firebase.database.DataSnapshot
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val languageIdentificationLiveData = mutableLiveData<String>()
    val profilesLiveData = mutableLiveData<List<TextProfile>>()

    fun identifyLanguage(text: String) {
        MlKitRepository.identifyLanguage(text, languageIdentificationLiveData)
    }

    fun getProfiles() {
        FirebaseDbRepository.getProfiles(profilesLiveData)
    }
}