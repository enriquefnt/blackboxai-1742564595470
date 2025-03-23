package com.example.caseapp.network

import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.data.model.User
import retrofit2.http.*

interface ApiService {
    
    @POST("auth/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): LoginResponse

    @GET("cases")
    suspend fun getCases(
        @Header("Authorization") token: String,
        @Query("lastSync") lastSync: Long? = null
    ): List<Case>

    @GET("cases/{id}")
    suspend fun getCase(
        @Header("Authorization") token: String,
        @Path("id") caseId: Int
    ): Case

    @POST("cases")
    suspend fun createCase(
        @Header("Authorization") token: String,
        @Body case: Case
    ): Case

    @PUT("cases/{id}")
    suspend fun updateCase(
        @Header("Authorization") token: String,
        @Path("id") caseId: Int,
        @Body case: Case
    ): Case

    @DELETE("cases/{id}")
    suspend fun deleteCase(
        @Header("Authorization") token: String,
        @Path("id") caseId: Int
    )

    @GET("cases/{caseId}/controls")
    suspend fun getControls(
        @Header("Authorization") token: String,
        @Path("caseId") caseId: Int,
        @Query("lastSync") lastSync: Long? = null
    ): List<Control>

    @GET("cases/{caseId}/controls/{controlId}")
    suspend fun getControl(
        @Header("Authorization") token: String,
        @Path("caseId") caseId: Int,
        @Path("controlId") controlId: Int
    ): Control

    @POST("cases/{caseId}/controls")
    suspend fun createControl(
        @Header("Authorization") token: String,
        @Path("caseId") caseId: Int,
        @Body control: Control
    ): Control

    @PUT("cases/{caseId}/controls/{controlId}")
    suspend fun updateControl(
        @Header("Authorization") token: String,
        @Path("caseId") caseId: Int,
        @Path("controlId") controlId: Int,
        @Body control: Control
    ): Control

    @DELETE("cases/{caseId}/controls/{controlId}")
    suspend fun deleteControl(
        @Header("Authorization") token: String,
        @Path("caseId") caseId: Int,
        @Path("controlId") controlId: Int
    )

    @GET("sync")
    suspend fun sync(
        @Header("Authorization") token: String,
        @Query("lastSync") lastSync: Long
    ): SyncResponse

    data class LoginResponse(
        val token: String,
        val user: User
    )

    data class SyncResponse(
        val timestamp: Long,
        val cases: List<Case>,
        val controls: List<Control>,
        val deletedCases: List<Int>,
        val deletedControls: List<Int>
    )
}