package com.atulya.criminalintent.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {
    private val TAG = ">>> DatePickerFragment"

    private val args: DatePickerFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        
        val dateListner = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->

            val  resultDate = GregorianCalendar(year, month, day).time

            setFragmentResult(
                DATE_REQUEST_KEY,
                bundleOf(DATE_BUNDLE_KEY to resultDate) // k: v pair
            )
        }
        
        val cal = Calendar.getInstance()
        cal.time = args.currentDate
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        Log.d(TAG, "onCreateDialog: ${args.currentDate}")
        return DatePickerDialog(
            requireContext(),
            dateListner,
            year,
            month,
            day
        )
    }
    
    companion object{
        const val DATE_REQUEST_KEY = "DATE_REQUEST_KEY"
        const val DATE_BUNDLE_KEY = "DATE_BUNDLE_KEY"
    }
}