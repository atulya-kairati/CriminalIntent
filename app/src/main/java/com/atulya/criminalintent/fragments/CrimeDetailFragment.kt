package com.atulya.criminalintent.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.atulya.criminalintent.R
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.databinding.FragmentCrimeDetailBinding
import com.atulya.criminalintent.models.CrimeDetailViewModel
import com.atulya.criminalintent.models.CrimeDetailViewModelFactory
import kotlinx.coroutines.launch
import java.util.*


private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeDetailFragment : Fragment() {

    private val TAG = ">>> CrimeDetailFragment"

    private var _binding: FragmentCrimeDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null. Check if the view is visible"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    private val selectSuspect = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri ->
        Log.d(TAG, "contact uri: $uri")
        uri?.let {contactUri: Uri ->
            parseContactSelection(contactUri)
        }
    }

    private fun parseContactSelection(uri: Uri) {
        val queryField = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireContext().contentResolver
            .query(uri, queryField, null, null, null)

        queryCursor?.use { cursor ->
            if(cursor.moveToFirst()){
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect=suspect)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        https://developer.android.com/guide/navigation/navigation-custom-back
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            Log.d(TAG, "onCreate: $this")

            if (binding.crimeTitle.text.isBlank()) {
                Toast.makeText(context, "Title can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().popBackStack()
            }
        }
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
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }


            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            crimeSuspect.setOnClickListener {
                selectSuspect.launch(null)
            }

            /**
             * creates intent for the contact app launch
             * then checks if the intent can be resolved
             * Acc. enables/disables the crimeSuspect button
             */
            val selectSuspectIntent = selectSuspect.contract
                .createIntent(requireContext(), null)
            Log.d(TAG, "selectSuspectIntent: $selectSuspectIntent")

            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    Log.d(TAG, "onViewCreated: $crime")
                    crime?.let {
                        updateUi(crime)
                        // TODO: Since the ui is updated when we change the edittext
                        // the cursor jumps forward.
                    }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.DATE_REQUEST_KEY) { requestKey, bundle ->
            val date = bundle.getSerializable(DatePickerFragment.DATE_BUNDLE_KEY) as Date
            Log.d(TAG, "onViewCreated: $date")
            crimeDetailViewModel.updateCrime {
                it.copy(date = date)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime) {
        binding.apply {
            crimeTitle.setText(crime.title)
            crimeTitle.setSelection(crimeTitle.text.length)

            crimeDate.text = crime.date.toString()

            crimeSolved.isChecked = crime.isSolved

            // We are setting listening here because
            // this is the only place where we have
            // access to the latest crime
            crimeDate.setOnClickListener {
                findNavController().navigate(
                    CrimeDetailFragmentDirections.selectDate(crime.date)
                )
            }

            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject)
                    )
                }

                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.send_report)
                )

                startActivity(chooserIntent)
            }

            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.choose_suspect)
            }
        }

    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )
    }

    private fun canResolveIntent(intent: Intent): Boolean{
        /**
         * We have provided a disclosure in the manifest
         * And through [PackageManger] we can determine if the
         * OS can resolve our intent or not.
         */
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )

        Log.d(TAG, "resolvedActivity: $resolvedActivity")

        return resolvedActivity != null
    }
}