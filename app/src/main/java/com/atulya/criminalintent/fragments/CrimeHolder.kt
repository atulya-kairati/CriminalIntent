package com.atulya.criminalintent.fragments

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.databinding.ListItemCrimeBinding
import com.atulya.criminalintent.databinding.ListItemCriticalCrimeBinding
import java.text.DateFormat
import java.util.UUID

class CrimeHolder(
    val binding: ListItemCrimeBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit){
        binding.crimeTitleText.text = crime.title
        binding.crimeDateText.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)



        binding.root.setOnClickListener{
            onCrimeClicked(crime.id)
        }

        binding.crimeSolved.isVisible = crime.isSolved
    }

}

//class SpecialCrimeHolder(
//    val binding: ListItemCriticalCrimeBinding
//): RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(crimes: Crime){
//        binding.crimeTitleText.text = crimes.title
//        binding.crimeDateText.text = crimes.date.toString()
//        binding.policeRequiredText.text = if(crimes.requiresPolice)
//                                            "Police Required"
//                                            else "Police Not required"
//
//        binding.root.setOnClickListener{
//            Toast.makeText(
//                binding.root.context,
//                "${crimes.title} clicked Special",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//}