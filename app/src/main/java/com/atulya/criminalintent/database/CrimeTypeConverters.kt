package com.atulya.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

class CrimeTypeConverters {

    @TypeConverter
    fun fromDate(date: Date) = date.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long) = Date(millisSinceEpoch)
}