package com.acostadev.acostademo.utils

import java.util.*

object LocaleUtils {

    private val localeList = Locale.getAvailableLocales()

    fun getDisplayName(code: String): String {
        localeList.find { it.language == code }?.let { return it.displayName }
        return code
    }
}