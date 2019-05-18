package com.acostadev.acostademo.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acostadev.acostademo.models.TextProfile
import com.acostadev.acostademo.mutableLiveData
import com.acostadev.acostademo.repositories.FirebaseDbRepository
import com.acostadev.acostademo.repositories.MlKitRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    private val TAG = "MainViewModel"
    val languageIdentificationLiveData = mutableLiveData<String>()
    val profilesLiveData = mutableLiveData<List<TextProfile>>()

    fun identifyLanguage(text: String) {
        MlKitRepository.identifyLanguage(text, languageIdentificationLiveData)
    }

    fun getProfiles() {
        FirebaseDbRepository.getProfiles(profilesLiveData)
    }

    fun doLongTask() {
        viewModelScope.launch {
            doHeavyJob()
        }
    }

    private suspend fun doHeavyJob() = withContext(Dispatchers.IO) {
        Log.d(TAG, "Performing long task")
        Thread.sleep(10000)
        Log.d(TAG, "Long task finished")
    }
}