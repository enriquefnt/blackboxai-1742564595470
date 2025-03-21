package com.example.caseapp.data.local

import androidx.room.*
import com.example.caseapp.data.model.Control
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ControlDao {
    @Query("SELECT * FROM controls WHERE controlId = :controlId")
    suspend fun getControlById(controlId: Int): Control?

    @Query("""
        SELECT * FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1 
        ORDER BY fechaControl DESC
    """)
    fun getControlsForCase(caseId: Int): Flow<List<Control>>

    @Query("""
        SELECT * FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1 
        AND fechaControl BETWEEN :startDate AND :endDate
        ORDER BY fechaControl DESC
    """)
    fun getControlsForCaseInRange(
        caseId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<Control>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertControl(control: Control): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertControls(controls: List<Control>)

    @Update
    suspend fun updateControl(control: Control)

    @Delete
    suspend fun deleteControl(control: Control)

    @Query("DELETE FROM controls WHERE controlId = :controlId")
    suspend fun deleteControlById(controlId: Int)

    @Query("UPDATE controls SET synced = :synced WHERE controlId = :controlId")
    suspend fun updateSyncStatus(controlId: Int, synced: Boolean)

    @Query("SELECT * FROM controls WHERE synced = 0")
    suspend fun getUnsyncedControls(): List<Control>

    @Query("""
        UPDATE controls 
        SET activo = 0,
            fechaModificacion = :fechaModificacion,
            synced = 0
        WHERE controlId = :controlId
    """)
    suspend fun deactivateControl(
        controlId: Int, 
        fechaModificacion: Long = System.currentTimeMillis()
    )

    @Transaction
    suspend fun insertOrUpdateControl(control: Control) {
        val existingControl = getControlById(control.controlId)
        if (existingControl == null) {
            insertControl(control)
        } else {
            updateControl(control)
        }
    }

    @Query("""
        SELECT * FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1 
        ORDER BY fechaControl DESC 
        LIMIT 1
    """)
    suspend fun getLastControlForCase(caseId: Int): Control?

    @Query("""
        SELECT COUNT(*) FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1
    """)
    suspend fun getControlCountForCase(caseId: Int): Int

    @Query("""
        SELECT * FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1 
        AND fechaControl <= :date 
        ORDER BY fechaControl DESC 
        LIMIT 1
    """)
    suspend fun getControlAtDate(caseId: Int, date: Date): Control?

    @Query("""
        SELECT * FROM controls 
        WHERE caseId = :caseId 
        AND activo = 1 
        AND proximoCitaRecomendada IS NOT NULL 
        AND proximoCitaRecomendada > :date 
        ORDER BY proximoCitaRecomendada ASC 
        LIMIT 1
    """)
    suspend fun getNextScheduledControl(caseId: Int, date: Date = Date()): Control?
}