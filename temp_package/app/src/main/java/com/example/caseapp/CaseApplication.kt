package com.example.caseapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.caseapp.worker.SyncWorker
import java.util.concurrent.TimeUnit

class CaseApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        setupWorkManager()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    private fun setupWorkManager() {
        // Configurar restricciones para la sincronización
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Crear solicitud de trabajo periódico
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        // Programar trabajo de sincronización
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Mantener trabajo existente si ya está programado
            syncRequest
        )
    }
}