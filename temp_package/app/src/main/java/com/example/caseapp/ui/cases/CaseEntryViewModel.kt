package com.example.caseapp.ui.cases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.data.model.Case
import com.example.caseapp.network.NetworkResult
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.NetworkManager
import com.example.caseapp.util.PreferencesManager
import kotlinx.coroutines.launch
import java.util.*

class CaseEntryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val apiService = RetrofitClient.getInstance(application)
    private val preferencesManager = PreferencesManager(application)
    private val networkManager = NetworkManager.getInstance(application)

    private val _uiState = MutableLiveData<CaseEntryUiState>(CaseEntryUiState.Initial)
    val uiState: LiveData<CaseEntryUiState> = _uiState

    private var existingCase: Case? = null

    fun loadCase(caseId: Int) {
        if (caseId == 0) return // Nuevo caso

        viewModelScope.launch {
            try {
                val case = database.caseDao().getCaseById(caseId)
                case?.let {
                    existingCase = it
                    _uiState.value = CaseEntryUiState.Loaded(it)
                }
            } catch (e: Exception) {
                _uiState.value = CaseEntryUiState.Error(e.message ?: "Error al cargar el caso")
            }
        }
    }

    fun saveCase(
        nombre: String,
        numeroDocumento: String,
        fechaNacimiento: Date,
        sexo: Int
    ) {
        if (!validateInput(nombre, numeroDocumento)) {
            return
        }

        viewModelScope.launch {
            _uiState.value = CaseEntryUiState.Loading

            try {
                val userId = preferencesManager.getCurrentUserId()
                val case = existingCase?.copy(
                    nombre = nombre,
                    numeroDocumento = numeroDocumento,
                    fechaNacimiento = fechaNacimiento,
                    sexo = sexo,
                    modifiedBy = userId,
                    fechaModificacion = Date(),
                    synced = false
                ) ?: Case(
                    nombre = nombre,
                    numeroDocumento = numeroDocumento,
                    fechaNacimiento = fechaNacimiento,
                    sexo = sexo,
                    createdBy = userId,
                    synced = false
                )

                // Guardar en base de datos local
                val caseId = database.caseDao().insertOrUpdateCase(case)

                // Si hay conexión, intentar sincronizar inmediatamente
                if (networkManager.isNetworkAvailable()) {
                    val token = preferencesManager.getAuthToken() ?: return@launch

                    val result = if (existingCase == null) {
                        safeApiCall {
                            apiService.createCase("Bearer $token", case)
                        }
                    } else {
                        safeApiCall {
                            apiService.updateCase("Bearer $token", case.caseId, case)
                        }
                    }

                    when (result) {
                        is NetworkResult.Success -> {
                            database.caseDao().updateSyncStatus(caseId.toInt(), true)
                            _uiState.value = CaseEntryUiState.Success
                        }
                        is NetworkResult.Error -> {
                            // El caso se guardó localmente pero no se pudo sincronizar
                            _uiState.value = CaseEntryUiState.SuccessWithSync(
                                "Los datos se guardarán cuando haya conexión"
                            )
                        }
                        is NetworkResult.Loading -> {
                            // No necesitamos manejar este estado aquí
                        }
                    }
                } else {
                    // Sin conexión, el caso se guardó localmente
                    _uiState.value = CaseEntryUiState.SuccessWithSync(
                        "Los datos se guardarán cuando haya conexión"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CaseEntryUiState.Error(e.message ?: "Error al guardar el caso")
            }
        }
    }

    private fun validateInput(nombre: String, numeroDocumento: String): Boolean {
        if (nombre.isBlank() || numeroDocumento.isBlank()) {
            _uiState.value = CaseEntryUiState.Error("Por favor complete todos los campos")
            return false
        }
        return true
    }

    sealed class CaseEntryUiState {
        object Initial : CaseEntryUiState()
        object Loading : CaseEntryUiState()
        object Success : CaseEntryUiState()
        data class SuccessWithSync(val message: String) : CaseEntryUiState()
        data class Loaded(val case: Case) : CaseEntryUiState()
        data class Error(val message: String) : CaseEntryUiState()
    }
}