package com.crypto.ticker.ui.home.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crypto.ticker.core.common.BaseViewModel
import com.crypto.ticker.data.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository) : BaseViewModel() {

    private val _isDarkMode = MutableLiveData(repository.isDarkModeEnabled())
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    fun onThemeChanged(isDarkMode: Boolean) {
        repository.setThemeMode(isDarkMode)
        this@SettingsViewModel._isDarkMode.value = isDarkMode
    }
}