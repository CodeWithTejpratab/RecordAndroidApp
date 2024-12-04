package com.example.recordkeeper.roomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Record::class], version = 1, exportSchema = false)
@TypeConverters(RecordTypeConverters::class)
abstract class RecordLogDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao

    companion object {
        @Volatile
        private var INSTANCE: RecordLogDatabase? = null

        fun getDatabase(context: Context): RecordLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordLogDatabase::class.java,
                    "record_log_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

