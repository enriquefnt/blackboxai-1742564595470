package com.example.caseapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.data.model.Case
import com.example.caseapp.network.ApiService
import com.example.caseapp.network.NetworkResult
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.NetworkManager
import com.example.caseapp.util.PreferencesManager
import com.example.caseapp.worker.SyncWorker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val apiService = RetrofitClient.getInstance(application)
    private val preferencesManager = PreferencesManager(application)
    private val networkManager = NetworkManager.getInstance(application)

    private val _uiState = MutableLiveData<MainUiState>()
    val uiState: LiveData<MainUiState> = _uiState

    private val _syncState = MutableLiveData<SyncState>()
    val syncState: LiveData<SyncState> = _syncState

    init {
        observeNetworkState()
        observeCases()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkManager.connectivityFlow.collect { isAvailable ->
                if (isAvailable) {
                    _uiState.value = MainUiState.Online
                    // Intentar sincronizar datos pendientes
                    SyncWorker.startOneTimeSync(getApplication())
                } else {
                    _uiState.value = MainUiState.Offline
                }
            }
        }
    }

    private fun observeCases() {
        viewModelScope.launch {
            database.caseDao().getAllCases().collect { cases ->
                when (val currentState = _uiState.value) {
                    is MainUiState.Online -> _uiState.value = MainUiState.Online.copy(cases = cases)
                    is MainUiState.Offline -> _uiState.value = MainUiState.Offline.copy(cases = cases)
                    else -> _uiState.value = MainUiState.Online(cases = cases)
                }
            }
        }
    }

    fun syncData() {
        if (!networkManager.isNetworkAvailable()) {
            _syncState.value = SyncState.Error("No hay conexión a Internet")
            return
        }

        viewModelScope.launch {
            _syncState.value = SyncState.Loading

            try {
                val token = preferencesManager.getAuthToken()
                    ?: return@launch.also { _syncState.value = SyncState.Error("No hay sesión activa") }

                val result = safeApiCall {
                    apiService.getCases("Bearer $token")
                }

                when (result) {
                    is NetworkResult.Success -> {
                        result.data.forEach { case ->
                            database.caseDao().insertOrUpdateCase(case)
                        }
                        _syncState.value = SyncState.Success
                        preferencesManager.setLastSyncTimestamp(System.currentTimeMillis())
                    }
                    is NetworkResult.Error -> {
                        _syncState.value = SyncState.Error(
                            result.exception.message ?: "Error de sincronización"
                        )
                    }
                    is NetworkResult.Loading -> {
                        _syncState.value = SyncState.Loading
                    }
                }
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.clearSession()
            database.clearAllTables()
        }
    }

    sealed class MainUiState {
        abstract val cases: List<Case>

        data class Online(
            override val cases: List<Case> = emptyList()
        ) : MainUiState()

        data class Offline(
            override val cases: List<Case> = emptyList()
        ) : MainUiState()
    }

    sealed class SyncState {
        object Loading : SyncState()
        object Success : SyncState()
        data class Error(val message: String) : SyncState()
    }
}