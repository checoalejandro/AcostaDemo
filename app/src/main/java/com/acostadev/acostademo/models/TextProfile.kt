package com.acostadev.acostademo.models

data class TextProfile(val label: String, val words: List<String>) {
    fun getAllWords() = words.joinToString(", ")
}