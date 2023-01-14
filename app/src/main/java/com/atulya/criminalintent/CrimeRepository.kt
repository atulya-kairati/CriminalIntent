package com.atulya.criminalintent

import android.content.Context
import androidx.room.Room
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

private const val DB_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DB_NAME
    ).createFromAsset(DB_NAME).build()

    fun getCrimes() = database.crimeDao().getCrimes()

    suspend fun getCrime(id: UUID) = database.crimeDao().getCrime(id)

    fun updateCrime(crime: Crime) {
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get() = checkNotNull(INSTANCE) {
            "CrimeRepository must be initialized first."
        }
    }

}