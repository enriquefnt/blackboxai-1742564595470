package com.example.caseapp.ui.cases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.network.ApiService
import com.example.caseapp.network.NetworkResult
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.AgeCalculator
import com.example.caseapp.util.NetworkManager
import com.example.caseapp.util.PreferencesManager
import com.example.caseapp.util.ZScoreCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CaseDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val apiService = RetrofitClient.getInstance(application)
    private val preferencesManager = PreferencesManager.getInstance(application)
    private val networkManager = NetworkManager.getInstance(application)
    private val zScoreCalculator = ZScoreCalculator(application)

    private val _uiState = MutableStateFlow<CaseDetailsUiState>(CaseDetailsUiState.Loading)
    val uiState: StateFlow<CaseDetailsUiState> = _uiState

    private val _controls = MutableLiveData<List<Control>>()
    val controls: LiveData<List<Control>> = _controls

    fun loadCase(caseId: Int) {
        viewModelScope.launch {
            _uiState.value = CaseDetailsUiState.Loading

            try {
                // Cargar caso desde la base de datos local
                val case = database.caseDao().getCaseById(caseId)
                if (case != null) {
                    _uiState.value = CaseDetailsUiState.Success(case)
                    loadControls(caseId)

                    // Si hay conexión, sincronizar con el servidor
                    if (networkManager.isNetworkAvailable()) {
                        syncWithServer(caseId)
                    }
                } else {
                    _uiState.value = CaseDetailsUiState.Error("Caso no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = CaseDetailsUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    private suspend fun syncWithServer(caseId: Int) {
        val token = preferencesManager.getAuthToken() ?: return

        when (val result = safeApiCall { apiService.getCase("Bearer $token", caseId) }) {
            is NetworkResult.Success -> {
                database.caseDao().insertOrUpdateCase(result.data)
                _uiState.value = CaseDetailsUiState.Success(result.data)

                // Sincronizar controles
                val controlsResult = safeApiCall {
                    apiService.getControls("Bearer $token", caseId)
                }
                if (controlsResult is NetworkResult.Success) {
                    database.controlDao().insertControls(controlsResult.data)
                    loadControls(caseId)
                }
            }
            is NetworkResult.Error -> {
                // No mostrar error ya que tenemos datos locales
            }
            is NetworkResult.Loading -> {
                // No hacer nada
            }
        }
    }

    private suspend fun loadControls(caseId: Int) {
        database.controlDao().getControlsForCase(caseId).collect { controls ->
            _controls.value = controls
        }
    }

    fun calculateAge(birthDate: Date, currentDate: Date = Date()): String {
        return AgeCalculator.calculateAge(birthDate, currentDate).formattedString
    }

    fun calculateZScores(control: Control, case: Case): Map<String, Float> {
        return zScoreCalculator.calculateAllZScores(
            control = control,
            sex = case.sexo,
            fechaNace = case.fechaNacimiento,
            fechaControl = control.fechaControl
        )
    }

    sealed class CaseDetailsUiState {
        object Loading : CaseDetailsUiState()
        data class Success(val case: Case) : CaseDetailsUiState()
        data class Error(val message: String) : CaseDetailsUiState()
    }
}