package com.example.caseapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.caseapp.data.model.Case
import com.example.caseapp.data.model.Control
import com.example.caseapp.data.model.User

@Database(
    entities = [
        User::class,
        Case::class,
        Control::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun caseDao(): CaseDao
    abstract fun controlDao(): ControlDao

    companion object {
        private const val DATABASE_NAME = "case_app_db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .addCallback(object : RoomDatabase.Callback() {
                // Aquí podríamos agregar lógica de inicialización si es necesario
            })
            .fallbackToDestructiveMigration() // En producción, implementar migraciones adecuadas
            .build()
        }
    }
}