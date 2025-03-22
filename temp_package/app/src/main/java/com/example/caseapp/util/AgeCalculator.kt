package com.example.caseapp.util

import java.util.*

class AgeCalculator {
    data class Age(
        val years: Int,
        val months: Int,
        val days: Int
    ) {
        val formattedString: String
            get() = when {
                years > 0 -> {
                    if (months > 0) {
                        "$years años $months meses"
                    } else {
                        "$years años"
                    }
                }
                months > 0 -> {
                    if (days > 0) {
                        "$months meses $days días"
                    } else {
                        "$months meses"
                    }
                }
                else -> "$days días"
            }
    }

    companion object {
        fun calculateAge(birthDate: Date, currentDate: Date = Date()): Age {
            val calendar1 = Calendar.getInstance()
            val calendar2 = Calendar.getInstance()

            calendar1.time = birthDate
            calendar2.time = currentDate

            var years = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR)
            var months = calendar2.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH)
            var days = calendar2.get(Calendar.DAY_OF_MONTH) - calendar1.get(Calendar.DAY_OF_MONTH)

            if (days < 0) {
                calendar2.add(Calendar.MONTH, -1)
                days += calendar2.getActualMaximum(Calendar.DAY_OF_MONTH)
                months--
            }

            if (months < 0) {
                months += 12
                years--
            }

            return Age(years, months, days)
        }

        fun calculateAgeInMonths(birthDate: Date, currentDate: Date = Date()): Int {
            val age = calculateAge(birthDate, currentDate)
            return age.years * 12 + age.months
        }

        fun calculateAgeInDays(birthDate: Date, currentDate: Date = Date()): Int {
            return ((currentDate.time - birthDate.time) / (1000 * 60 * 60 * 24)).toInt()
        }
    }
}