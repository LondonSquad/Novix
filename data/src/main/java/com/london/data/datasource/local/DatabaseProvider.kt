package com.london.data.datasource.local

import android.content.Context
import androidx.room.Room
import com.london.data.datasource.local.dto.SearchActorsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

            initializeDefaultData(instance)
            instance
        }
    }

    private fun initializeDefaultData(database: NovixDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = database.searchActorsDao()
                    dao.insert(
                        SearchActorsResponse(
                            date = System.currentTimeMillis(),
                            page = 1,
                            results = emptyList(),
                            totalPages = 1,
                            totalResults = 0,
                        )
                    )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}