package com.example.caseapp.ui.cases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.network.NetworkResult
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.NetworkManager
import com.example.caseapp.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CasesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val apiService = RetrofitClient.getInstance(application)
    private val preferencesManager = PreferencesManager(application)
    private val networkManager = NetworkManager.getInstance(application)

    private val _uiState = MutableStateFlow<CasesUiState>(CasesUiState.Loading)
    val uiState: StateFlow<CasesUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")

    init {
        loadCases()
        observeSearchQuery()
    }

    private fun loadCases() {
        viewModelScope.launch {
            try {
                database.caseDao().getAllCases().collect { cases ->
                    if (cases.isEmpty()) {
                        _uiState.value = CasesUiState.Empty
                    } else {
                        _uiState.value = CasesUiState.Success(cases)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CasesUiState.Error(e.message ?: "Error al cargar los casos")
            }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.collect { query ->
                if (query.isNotBlank()) {
                    searchCases(query)
                } else {
                    loadCases()
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun searchCases(query: String) {
        viewModelScope.launch {
            try {
                database.caseDao().searchCases(query).collect { cases ->
                    if (cases.isEmpty()) {
                        _uiState.value = CasesUiState.Empty
                    } else {
                        _uiState.value = CasesUiState.Success(cases)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CasesUiState.Error(e.message ?: "Error en la búsqueda")
            }
        }
    }

    fun getLastControl(caseId: Int): LiveData<Control?> {
        val result = MutableLiveData<Control?>()
        viewModelScope.launch {
            try {
                val control = database.controlDao().getLastControlForCase(caseId)
                result.value = control
            } catch (e: Exception) {
                result.value = null
            }
        }
        return result
    }

    fun deleteCase(case: Case) {
        viewModelScope.launch {
            try {
                val userId = preferencesManager.getCurrentUserId()
                database.caseDao().softDeleteCase(case.caseId, userId)

                // Si hay conexión, intentar sincronizar el cambio
                if (networkManager.isNetworkAvailable()) {
                    val token = preferencesManager.getAuthToken() ?: return@launch
                    
                    val result = safeApiCall {
                        apiService.deleteCase("Bearer $token", case.caseId)
                    }

                    when (result) {
                        is NetworkResult.Success -> {
                            // Caso eliminado exitosamente en el servidor
                        }
                        is NetworkResult.Error -> {
                            // Marcar para sincronización posterior
                            database.caseDao().updateSyncStatus(case.caseId, false)
                        }
                        is NetworkResult.Loading -> {
                            // Estado de carga, no necesitamos manejarlo aquí
                        }
                    }
                } else {
                    // Sin conexión, marcar para sincronización posterior
                    database.caseDao().updateSyncStatus(case.caseId, false)
                }
            } catch (e: Exception) {
                _uiState.value = CasesUiState.Error(e.message ?: "Error al eliminar el caso")
            }
        }
    }

    sealed class CasesUiState {
        object Loading : CasesUiState()
        object Empty : CasesUiState()
        data class Success(val cases: List<Case>) : CasesUiState()
        data class Error(val message: String) : CasesUiState()
    }
}