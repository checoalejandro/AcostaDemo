package com.acostadev.acostademo.main

import androidx.lifecycle.ViewModel
import com.acostadev.acostademo.mutableLiveData
import com.acostadev.acostademo.repositories.MlKitRepository
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    val languageIdentificationLiveData = mutableLiveData<String>()

    fun identifyLanguage(text: String) {
        MlKitRepository.identifyLanguage(text, languageIdentificationLiveData)
    }
}