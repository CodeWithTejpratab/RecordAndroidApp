package com.example.recordkeeper.roomDataBase

import androidx.room.*

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateRecord(record: Record)

    @Query("SELECT * FROM Record WHERE type = :type")
    suspend fun getRecordByType(type: RecordType): Record?

    @Query("SELECT * FROM Record ORDER BY type ASC")
    suspend fun getAllRecords(): List<Record>

    @Query("UPDATE Record SET time = :time, date = :date WHERE type = :type")
    suspend fun updateRecord(type: RecordType, time: String, date: String)
}

