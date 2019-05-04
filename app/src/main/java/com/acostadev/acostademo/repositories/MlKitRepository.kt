package com.acostadev.acostademo.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acostadev.acostademo.executors.AppExecutors
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification

object MlKitRepository {

    private val languageIdentifier: FirebaseLanguageIdentification = FirebaseNaturalLanguage.getInstance().languageIdentification

    fun identifyLanguage(text: String, liveData: MutableLiveData<String>) {
        AppExecutors.diskIO().execute {
            languageIdentifier.identifyLanguage(text)
                    .addOnSuccessListener { languageCode ->
                        AppExecutors.mainThread().execute {
                            liveData.value = languageCode
                        }
                    }
        }
    }
}