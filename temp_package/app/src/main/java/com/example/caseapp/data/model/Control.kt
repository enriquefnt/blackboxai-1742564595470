package com.example.caseapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "controls",
    foreignKeys = [
        ForeignKey(
            entity = Case::class,
            parentColumns = ["caseId"],
            childColumns = ["caseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("caseId"),
        Index("fechaControl")
    ]
)
data class Control(
    @PrimaryKey(autoGenerate = true)
    val controlId: Int = 0,
    
    val caseId: Int,
    
    val userId: Int,
    
    val fechaControl: Date,
    
    // Medidas antropométricas
    val peso: Float,  // en kilogramos
    
    val talla: Float,  // en centímetros
    
    val perimetroCefalico: Float? = null,  // en centímetros
    
    // Z-scores calculados
    val zScorePeso: Float? = null,
    
    val zScoreTalla: Float? = null,
    
    val zScoreIMC: Float? = null,
    
    val zScorePC: Float? = null,
    
    // Datos adicionales
    val observaciones: String = "",
    
    val diagnostico: String = "",
    
    val tratamiento: String = "",
    
    val proximoCitaRecomendada: Date? = null,
    
    val activo: Boolean = true,
    
    val fechaCreacion: Date = Date(),
    
    val fechaModificacion: Date = Date(),
    
    val synced: Boolean = false
) {
    val imc: Float
        get() = if (talla > 0) {
            peso / ((talla / 100) * (talla / 100))
        } else {
            0f
        }

    companion object {
        // Constantes para interpretación de z-scores
        const val ZSCORE_NORMAL_MIN = -2f
        const val ZSCORE_NORMAL_MAX = 2f
        const val ZSCORE_RIESGO_MIN = -3f
        const val ZSCORE_RIESGO_MAX = 3f
        
        // Constantes para diagnósticos comunes
        const val DIAGNOSTICO_NORMAL = "NORMAL"
        const val DIAGNOSTICO_BAJO_PESO = "BAJO PESO"
        const val DIAGNOSTICO_SOBREPESO = "SOBREPESO"
        const val DIAGNOSTICO_OBESIDAD = "OBESIDAD"
        const val DIAGNOSTICO_TALLA_BAJA = "TALLA BAJA"
        const val DIAGNOSTICO_TALLA_ALTA = "TALLA ALTA"
    }
}