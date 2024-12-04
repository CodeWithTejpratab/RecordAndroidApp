package com.example.recordkeeper.roomDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey val type: RecordType,
    val time: String,
    val date: String
)

enum class RecordType(val displayName: String) {
    FiveKm("5km"),
    TenKm("10km"),
    HalfMarathon("Half-Marathon"),
    Marathon("Marathon"),
    LongestRide("Longest Ride"),
    BiggestClimb("Biggest Climb"),
    BestAverageSpeed("Best Average Speed");

    companion object {
        fun fromDisplayName(name: String): RecordType? {
            return entries.find { it.displayName == name }
        }
    }
}

