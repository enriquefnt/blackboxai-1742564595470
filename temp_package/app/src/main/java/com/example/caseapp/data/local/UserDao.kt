package com.example.caseapp.data.local

import androidx.room.*
import com.example.caseapp.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE activo = 1")
    fun getActiveUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users WHERE userId = :userId")
    suspend fun deleteUserById(userId: Int)

    @Query("UPDATE users SET synced = :synced WHERE userId = :userId")
    suspend fun updateSyncStatus(userId: Int, synced: Boolean)

    @Query("SELECT * FROM users WHERE synced = 0")
    suspend fun getUnsyncedUsers(): List<User>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Query("""
        UPDATE users 
        SET activo = 0,
            fechaModificacion = :fechaModificacion,
            synced = 0
        WHERE userId = :userId
    """)
    suspend fun deactivateUser(userId: Int, fechaModificacion: Long = System.currentTimeMillis())

    @Transaction
    suspend fun insertOrUpdateUser(user: User) {
        val existingUser = getUserById(user.userId)
        if (existingUser == null) {
            insertUser(user)
        } else {
            updateUser(user)
        }
    }

    @Query("""
        SELECT * FROM users 
        WHERE activo = 1 
        AND (
            lower(nombre) LIKE '%' || lower(:query) || '%' OR
            lower(apellido) LIKE '%' || lower(:query) || '%' OR
            lower(username) LIKE '%' || lower(:query) || '%' OR
            lower(email) LIKE '%' || lower(:query) || '%'
        )
        ORDER BY 
            CASE 
                WHEN lower(nombre) LIKE lower(:query) || '%' THEN 1
                WHEN lower(apellido) LIKE lower(:query) || '%' THEN 2
                ELSE 3
            END,
            nombre ASC,
            apellido ASC
    """)
    fun searchUsers(query: String): Flow<List<User>>
}