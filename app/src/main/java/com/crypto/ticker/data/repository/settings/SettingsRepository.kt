package com.crypto.ticker.data.repository.settings

import com.crypto.ticker.data.local.prefs.PreferenceStorage
import javax.inject.Inject

/**
 * [SettingsRepository] is to manage preference for dark mode option
 */
class SettingsRepository @Inject constructor(private val preferenceStorage: PreferenceStorage) {

    fun isDarkModeEnabled(): Boolean {
        return preferenceStorage.isDarkMode
    }

    fun setThemeMode(isDarkMode: Boolean) {
        preferenceStorage.isDarkMode = isDarkMode
    }
}