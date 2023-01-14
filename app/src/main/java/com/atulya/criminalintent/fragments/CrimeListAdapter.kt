package com.atulya.criminalintent.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.databinding.ListItemCrimeBinding
import java.util.UUID

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeId: UUID) -> Unit
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return 0

        // To use the different viewtypes
//        return if (crimes[position].requiresPolice) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)

        // To use the different viewtypes
//        return if(viewType == 0){
//            val inflater = LayoutInflater.from(parent.context)
//            val binding = ListItemCrimeBinding.inflate(inflater,parent,false)
//            CrimeHolder(binding)
//        }
//        else {
//            val inflater = LayoutInflater.from(parent.context)
//            val binding = ListItemCriticalCrimeBinding.inflate(inflater, parent, false)
//            SpecialCrimeHolder(binding)
//        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]


        // To use the different viewtypes
//        if(crimes[position].requiresPolice){
//            val specialHolder = holder as SpecialCrimeHolder
//            specialHolder.bind(crimes)
//        }
//        else {
//            val holder = holder as CrimeHolder
//            holder.bind(crimes)
//        }

        val holder = holder as CrimeHolder
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = crimes.size
}