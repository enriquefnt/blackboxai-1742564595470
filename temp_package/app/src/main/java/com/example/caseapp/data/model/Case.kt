package com.example.caseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "cases")
data class Case(
    @PrimaryKey(autoGenerate = true)
    val caseId: Int = 0,
    
    val userId: Int,
    
    val nombre: String,
    
    val apellido: String,
    
    val numeroDocumento: String,
    
    val tipoDocumento: String,
    
    // 1 = Masculino, 2 = Femenino
    val sexo: Int,
    
    val fechaNacimiento: Date,
    
    val direccion: String = "",
    
    val telefono: String = "",
    
    val nombreTutor: String = "",
    
    val telefonoTutor: String = "",
    
    val observaciones: String = "",
    
    val activo: Boolean = true,
    
    val fechaCreacion: Date = Date(),
    
    val fechaModificacion: Date = Date(),
    
    val synced: Boolean = false
) {
    val nombreCompleto: String
        get() = "$nombre $apellido"

    companion object {
        const val TIPO_DOCUMENTO_DNI = "DNI"
        const val TIPO_DOCUMENTO_CEDULA = "CEDULA"
        const val TIPO_DOCUMENTO_PASAPORTE = "PASAPORTE"
        
        const val SEXO_MASCULINO = 1
        const val SEXO_FEMENINO = 2
    }
}