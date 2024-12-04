package com.example.recordkeeper.roomDataBase

import androidx.room.TypeConverter

class RecordTypeConverters {
    @TypeConverter
    fun fromRecordType(type: RecordType): String {
        return type.name
    }

    @TypeConverter
    fun toRecordType(name: String): RecordType {
        return RecordType.valueOf(name)
    }
}
