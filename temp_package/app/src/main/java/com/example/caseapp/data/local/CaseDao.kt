package com.example.caseapp.data.local

import androidx.room.*
import com.example.caseapp.data.model.Case
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {
    @Query("SELECT * FROM cases WHERE caseId = :caseId")
    suspend fun getCaseById(caseId: Int): Case?

    @Query("SELECT * FROM cases WHERE activo = 1 ORDER BY fechaModificacion DESC")
    fun getActiveCases(): Flow<List<Case>>

    @Query("""
        SELECT * FROM cases 
        WHERE activo = 1 
        AND userId = :userId 
        ORDER BY fechaModificacion DESC
    """)
    fun getCasesByUserId(userId: Int): Flow<List<Case>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(case: Case): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCases(cases: List<Case>)

    @Update
    suspend fun updateCase(case: Case)

    @Delete
    suspend fun deleteCase(case: Case)

    @Query("DELETE FROM cases WHERE caseId = :caseId")
    suspend fun deleteCaseById(caseId: Int)

    @Query("UPDATE cases SET synced = :synced WHERE caseId = :caseId")
    suspend fun updateSyncStatus(caseId: Int, synced: Boolean)

    @Query("SELECT * FROM cases WHERE synced = 0")
    suspend fun getUnsyncedCases(): List<Case>

    @Query("""
        UPDATE cases 
        SET activo = 0,
            fechaModificacion = :fechaModificacion,
            synced = 0
        WHERE caseId = :caseId
    """)
    suspend fun deactivateCase(caseId: Int, fechaModificacion: Long = System.currentTimeMillis())

    @Transaction
    suspend fun insertOrUpdateCase(case: Case) {
        val existingCase = getCaseById(case.caseId)
        if (existingCase == null) {
            insertCase(case)
        } else {
            updateCase(case)
        }
    }

    @Query("""
        SELECT * FROM cases 
        WHERE activo = 1 
        AND (
            lower(nombre) LIKE '%' || lower(:query) || '%' OR
            lower(apellido) LIKE '%' || lower(:query) || '%' OR
            lower(numeroDocumento) LIKE '%' || lower(:query) || '%'
        )
        ORDER BY 
            CASE 
                WHEN lower(nombre) LIKE lower(:query) || '%' THEN 1
                WHEN lower(apellido) LIKE lower(:query) || '%' THEN 2
                WHEN numeroDocumento LIKE :query || '%' THEN 3
                ELSE 4
            END,
            nombre ASC,
            apellido ASC
    """)
    fun searchCases(query: String): Flow<List<Case>>

    @Query("""
        SELECT * FROM cases 
        WHERE activo = 1 
        AND userId = :userId
        AND (
            lower(nombre) LIKE '%' || lower(:query) || '%' OR
            lower(apellido) LIKE '%' || lower(:query) || '%' OR
            lower(numeroDocumento) LIKE '%' || lower(:query) || '%'
        )
        ORDER BY 
            CASE 
                WHEN lower(nombre) LIKE lower(:query) || '%' THEN 1
                WHEN lower(apellido) LIKE lower(:query) || '%' THEN 2
                WHEN numeroDocumento LIKE :query || '%' THEN 3
                ELSE 4
            END,
            nombre ASC,
            apellido ASC
    """)
    fun searchCasesByUserId(userId: Int, query: String): Flow<List<Case>>
}