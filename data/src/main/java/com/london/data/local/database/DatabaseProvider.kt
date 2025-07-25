package com.london.data.local.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: NovixDatabase? = null

    fun getDatabase(context: Context): NovixDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                NovixDatabase::class.java,
                "NovixDatabase"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}