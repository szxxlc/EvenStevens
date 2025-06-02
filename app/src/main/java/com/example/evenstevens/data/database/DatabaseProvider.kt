package com.example.evenstevens.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        // Jeśli instancja już istnieje, zwróć ją
        return INSTANCE ?: synchronized(this) {
            // Jeśli nie istnieje, utwórz nową instancję
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "evenstevens_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
