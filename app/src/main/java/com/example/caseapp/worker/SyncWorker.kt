package com.example.caseapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.caseapp.data.local.AppDatabase
import com.example.caseapp.network.RetrofitClient
import com.example.caseapp.network.safeApiCall
import com.example.caseapp.util.NetworkManager
import com.example.caseapp.util.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val database = AppDatabase.getInstance(context)
    private val apiService = RetrofitClient.getInstance(context)
    private val preferencesManager = PreferencesManager.getInstance(context)
    private val networkManager = NetworkManager.getInstance(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!networkManager.isNetworkAvailable()) {
                return@withContext Result.retry()
            }

            val token = preferencesManager.getAuthToken()
            if (token == null) {
                return@withContext Result.failure()
            }

            // Obtener timestamp de última sincronización
            val lastSync = preferencesManager.getLastSyncTimestamp()

            // Sincronizar con el servidor
            val result = safeApiCall {
                apiService.sync("Bearer $token", lastSync)
            }

            when (result) {
                is com.example.caseapp.network.NetworkResult.Success -> {
                    val response = result.data

                    // Actualizar casos y controles locales
                    database.runInTransaction {
                        // Eliminar casos borrados en el servidor
                        response.deletedCases.forEach { caseId ->
                            database.caseDao().deleteCaseById(caseId)
                        }

                        // Eliminar controles borrados en el servidor
                        response.deletedControls.forEach { controlId ->
                            database.controlDao().deleteControlById(controlId)
                        }

                        // Actualizar casos
                        response.cases.forEach { case ->
                            database.caseDao().insertOrUpdateCase(case)
                        }

                        // Actualizar controles
                        response.controls.forEach { control ->
                            database.controlDao().insertOrUpdateControl(control)
                        }
                    }

                    // Sincronizar casos locales no sincronizados
                    val unsyncedCases = database.caseDao().getUnsyncedCases()
                    unsyncedCases.forEach { case ->
                        val syncResult = safeApiCall {
                            if (case.caseId == 0) {
                                apiService.createCase("Bearer $token", case)
                            } else {
                                apiService.updateCase("Bearer $token", case.caseId, case)
                            }
                        }
                        if (syncResult is com.example.caseapp.network.NetworkResult.Success) {
                            database.caseDao().updateSyncStatus(case.caseId, true)
                        }
                    }

                    // Sincronizar controles locales no sincronizados
                    val unsyncedControls = database.controlDao().getUnsyncedControls()
                    unsyncedControls.forEach { control ->
                        val syncResult = safeApiCall {
                            if (control.controlId == 0) {
                                apiService.createControl("Bearer $token", control.caseId, control)
                            } else {
                                apiService.updateControl(
                                    "Bearer $token",
                                    control.caseId,
                                    control.controlId,
                                    control
                                )
                            }
                        }
                        if (syncResult is com.example.caseapp.network.NetworkResult.Success) {
                            database.controlDao().updateSyncStatus(control.controlId, true)
                        }
                    }

                    // Actualizar timestamp de última sincronización
                    preferencesManager.saveLastSyncTimestamp(response.timestamp)

                    Result.success()
                }
                is com.example.caseapp.network.NetworkResult.Error -> {
                    Result.retry()
                }
                is com.example.caseapp.network.NetworkResult.Loading -> {
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "sync_worker"
    }
}