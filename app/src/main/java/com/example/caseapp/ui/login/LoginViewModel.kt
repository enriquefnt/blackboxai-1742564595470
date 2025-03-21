package com.example.caseapp.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.network.ApiService
import com.example.caseapp.network.NetworkResult
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.PreferencesManager
import com.example.caseapp.worker.SyncWorker
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val apiService = RetrofitClient.getInstance(application)
    private val preferencesManager = PreferencesManager(application)

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    sealed class LoginState {
        object Initial : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                // Primero intentar login local
                val localUser = database.userDao().login(username, password)
                
                if (localUser != null) {
                    handleSuccessfulLogin(localUser.userId, username)
                    return@launch
                }

                // Si no hay usuario local, intentar login remoto
                val result = safeApiCall {
                    apiService.login(mapOf(
                        "username" to username,
                        "password" to password
                    ))
                }

                when (result) {
                    is NetworkResult.Success -> {
                        val response = result.data
                        // Guardar token y datos de usuario
                        preferencesManager.setAuthToken(response.token)
                        preferencesManager.setCurrentUser(
                            response.user.userId,
                            response.user.username
                        )

                        // Guardar usuario en base de datos local
                        database.userDao().insertUser(response.user)

                        // Iniciar sincronización
                        SyncWorker.startOneTimeSync(getApplication())

                        _loginState.value = LoginState.Success
                    }
                    is NetworkResult.Error -> {
                        _loginState.value = LoginState.Error(
                            result.exception.message ?: "Error de autenticación"
                        )
                    }
                    is NetworkResult.Loading -> {
                        _loginState.value = LoginState.Loading
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(
                    e.message ?: "Error inesperado"
                )
            }
        }
    }

    private fun handleSuccessfulLogin(userId: Int, username: String) {
        preferencesManager.setCurrentUser(userId, username)
        _loginState.value = LoginState.Success
        
        // Iniciar sincronización si hay conexión
        SyncWorker.startOneTimeSync(getApplication())
    }

    fun checkPreviousLogin(): Boolean {
        return preferencesManager.getAuthToken() != null
    }

    fun clearLoginState() {
        _loginState.value = LoginState.Initial
    }
}