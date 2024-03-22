package com.crypto.ticker.ui.projectProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crypto.ticker.api.Result
import com.crypto.ticker.core.common.BaseViewModel
import com.crypto.ticker.data.repository.projectProfile.ProjectProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectProfileViewModel @Inject constructor(private val repository: ProjectProfileRepository) :
    BaseViewModel() {

    fun projectBySymbol(symbol: String) = repository.projectBySymbol(symbol)

    private val _dataError = MutableLiveData<Boolean>()
    val dataError: LiveData<Boolean> = _dataError

    private val _historicalData = MutableLiveData<List<DoubleArray>>()
    val historicalData: LiveData<List<DoubleArray>> = _historicalData

    fun historicalData(symbol: String?) {
        symbol?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                val result = repository.historicalPrice(it)
                _isLoading.postValue(false)

                when (result) {
                    is Result.Success -> {
                        _historicalData.postValue(result.data.prices)
                        _dataError.postValue(false)
                    }
                    is Result.Error -> _dataError.postValue(true)
                }
            }
        }
    }

}