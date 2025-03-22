package com.example.caseapp.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromIntList(value: String?): List<Int> {
        return value?.split(",")?.filter { it.isNotEmpty() }?.map { it.toInt() } ?: emptyList()
    }

    @TypeConverter
    fun intListToString(list: List<Int>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromFloatList(value: String?): List<Float> {
        return value?.split(",")?.filter { it.isNotEmpty() }?.map { it.toFloat() } ?: emptyList()
    }

    @TypeConverter
    fun floatListToString(list: List<Float>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromBooleanList(value: String?): List<Boolean> {
        return value?.split(",")?.filter { it.isNotEmpty() }?.map { it.toBoolean() } ?: emptyList()
    }

    @TypeConverter
    fun booleanListToString(list: List<Boolean>?): String {
        return list?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun fromMap(value: String?): Map<String, Float> {
        return value?.split(",")?.filter { it.isNotEmpty() }?.associate {
            val (key, value) = it.split(":")
            key to value.toFloat()
        } ?: emptyMap()
    }

    @TypeConverter
    fun mapToString(map: Map<String, Float>?): String {
        return map?.entries?.joinToString(",") { "${it.key}:${it.value}" } ?: ""
    }
}