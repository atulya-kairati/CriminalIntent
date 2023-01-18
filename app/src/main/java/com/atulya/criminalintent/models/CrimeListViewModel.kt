package com.atulya.criminalintent.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atulya.criminalintent.CrimeRepository
import com.atulya.criminalintent.data.Crime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val TAG = ">>>CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(listOf()) // backing field
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow() // returns a backing field

    init {
        viewModelScope.launch {
            /** This is too provide the initial value to the [MutableStateFlow] */
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    suspend fun addNewCrime(crime: Crime) = crimeRepository.addCrime(crime)
}