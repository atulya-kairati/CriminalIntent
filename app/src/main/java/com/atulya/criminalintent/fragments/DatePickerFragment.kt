package com.atulya.criminalintent.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import java.util.Calendar

class DatePickerFragment : DialogFragment() {
    private val TAG = ">>> DatePickerFragment"

    private val args: DatePickerFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val cal = Calendar.getInstance()
        cal.time = args.currentDate
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "onCreateDialog: ${args.currentDate}")
        return DatePickerDialog(
            requireContext(),
            null,
            year,
            month,
            day
        )
    }
}