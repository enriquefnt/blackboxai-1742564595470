package com.example.caseapp.util

import android.content.Context
import com.example.caseapp.data.model.Control
import java.util.*

class ZScoreCalculator(private val context: Context) {

    fun calculateAllZScores(
        control: Control,
        sex: Int,
        fechaNace: Date,
        fechaControl: Date
    ): Map<String, Float> {
        val ageInMonths = AgeCalculator.calculateAgeInMonths(fechaNace, fechaControl)
        
        return mapOf(
            "zScorePeso" to calculateWeightForAge(control.peso, ageInMonths, sex),
            "zScoreTalla" to calculateHeightForAge(control.talla, ageInMonths, sex),
            "zScoreIMC" to calculateBMIForAge(control.peso, control.talla, ageInMonths, sex)
        )
    }

    private fun calculateWeightForAge(weight: Float, ageInMonths: Int, sex: Int): Float {
        // Aquí se implementaría la lógica para calcular el z-score de peso para edad
        // usando las tablas de la OMS
        // Por ahora retornamos un valor simulado
        return ((weight - getMedianWeight(ageInMonths, sex)) / 
                getWeightSD(ageInMonths, sex))
    }

    private fun calculateHeightForAge(height: Float, ageInMonths: Int, sex: Int): Float {
        // Aquí se implementaría la lógica para calcular el z-score de talla para edad
        // usando las tablas de la OMS
        // Por ahora retornamos un valor simulado
        return ((height - getMedianHeight(ageInMonths, sex)) / 
                getHeightSD(ageInMonths, sex))
    }

    private fun calculateBMIForAge(
        weight: Float,
        height: Float,
        ageInMonths: Int,
        sex: Int
    ): Float {
        val bmi = if (height > 0) {
            weight / ((height / 100) * (height / 100))
        } else {
            0f
        }
        
        // Aquí se implementaría la lógica para calcular el z-score de IMC para edad
        // usando las tablas de la OMS
        // Por ahora retornamos un valor simulado
        return ((bmi - getMedianBMI(ageInMonths, sex)) / 
                getBMISD(ageInMonths, sex))
    }

    // Funciones auxiliares para obtener valores de las tablas de la OMS
    // Estos valores deberían venir de archivos de recursos o una base de datos
    private fun getMedianWeight(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return when (sex) {
            1 -> 3.3f + (ageInMonths * 0.5f) // niños
            2 -> 3.2f + (ageInMonths * 0.45f) // niñas
            else -> 0f
        }
    }

    private fun getWeightSD(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return 0.5f + (ageInMonths * 0.02f)
    }

    private fun getMedianHeight(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return when (sex) {
            1 -> 50f + (ageInMonths * 2f) // niños
            2 -> 49f + (ageInMonths * 1.9f) // niñas
            else -> 0f
        }
    }

    private fun getHeightSD(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return 2f + (ageInMonths * 0.05f)
    }

    private fun getMedianBMI(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return when (sex) {
            1 -> 13f + (ageInMonths * 0.02f) // niños
            2 -> 13f + (ageInMonths * 0.018f) // niñas
            else -> 0f
        }
    }

    private fun getBMISD(ageInMonths: Int, sex: Int): Float {
        // Valor simulado
        return 1f + (ageInMonths * 0.01f)
    }

    companion object {
        // Constantes para interpretación de z-scores
        const val ZSCORE_NORMAL_MIN = -2f
        const val ZSCORE_NORMAL_MAX = 2f
        const val ZSCORE_RISK_MIN = -3f
        const val ZSCORE_RISK_MAX = 3f
    }
}