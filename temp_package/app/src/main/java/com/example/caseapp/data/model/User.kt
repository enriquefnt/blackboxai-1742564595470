package com.example.caseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: Int = 0,
    
    val username: String,
    
    val nombre: String,
    
    val apellido: String,
    
    val email: String,
    
    val rol: String,
    
    val establecimiento: String,
    
    val activo: Boolean = true,
    
    val fechaCreacion: Date = Date(),
    
    val fechaModificacion: Date = Date(),
    
    val synced: Boolean = false
) {
    val nombreCompleto: String
        get() = "$nombre $apellido"
        
    companion object {
        const val ROL_ADMIN = "ADMIN"
        const val ROL_MEDICO = "MEDICO"
        const val ROL_ENFERMERO = "ENFERMERO"
    }
}