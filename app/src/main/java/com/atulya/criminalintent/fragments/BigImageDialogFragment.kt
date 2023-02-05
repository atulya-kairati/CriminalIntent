package com.atulya.criminalintent.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.atulya.criminalintent.databinding.FragmentBigImageDialogBinding
import java.io.File

class BigImageDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentBigImageDialogBinding
    private val args: BigImageDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val photoName = args.photoName

        binding = FragmentBigImageDialogBinding.inflate(layoutInflater, container, false)
        binding.imageView.load(File(requireContext().filesDir, photoName))
        return binding.root
    }
}