package com.atulya.criminalintent.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.atulya.criminalintent.databinding.FragmentCrimeDetailBinding
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.models.CrimeDetailViewModel
import com.atulya.criminalintent.models.CrimeDetailViewModelFactory
import kotlinx.coroutines.launch

class CrimeDetailFragment : Fragment() {

    private val TAG = ">>> CrimeDetailFragment"

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "binding is null. Check if the view is visible"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Called 2nd
        // Keep onCreateView simple and only inflate and initiate binding here
        _binding = FragmentCrimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Called 3rd
        // Perform operations on views here (like adding listeners)
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged{ text,_,_,_->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            crimeDate.apply { isEnabled = false }

            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                crimeDetailViewModel.crime.collect{ crime ->
                    Log.d(TAG, "onViewCreated: $crime")
                    crime?.let {
                        updateUi(crime)
                        // TODO: Since the ui is updated when we change the edittext
                        // the cursor jumps forward.
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime){
        binding.apply {
            crimeTitle.setText(crime.title)

            crimeDate.text = crime.date.toString()

            crimeSolved.isChecked = crime.isSolved
        }
    }

}