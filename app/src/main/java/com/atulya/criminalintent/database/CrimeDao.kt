package com.atulya.criminalintent.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.atulya.criminalintent.data.Crime
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CrimeDao {

    /**
     * The @Dao annotation lets Room know that CrimeDao
       is one of your data access objects.

     * When you hook CrimeDao up to your database class,
       Room will generate implementations of the functions
       you add to this interface.
     */

    /**
     * A `flow` represents a asynchronous stream of data.

     * Throughout their lifetime, flows emit a sequence of
       values over an indefinite period of time that get sent to a collector.

     * The collector will observe the flow and will be notified every
       time a new value is emitted in the flow.

     * ***Usage***: Flows are a great tool for observing changes to a database.
     */

    @Query("SELECT * FROM crime")
    fun getCrimes(): Flow<List<Crime>>
    // We removed `suspend` because we don't need coroutine scope to reference a Flow
    // But we do need a coroutine scope when we are collecting the Flow stream.

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Crime

    @Update
    suspend fun updateCrime(crime: Crime)

    @Insert
    suspend fun addCrime(crime: Crime)
}
