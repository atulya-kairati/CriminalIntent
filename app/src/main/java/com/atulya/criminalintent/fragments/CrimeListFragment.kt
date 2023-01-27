package com.atulya.criminalintent.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.atulya.criminalintent.R
import com.atulya.criminalintent.data.Crime
import com.atulya.criminalintent.databinding.FragmentCrimeListBinding
import com.atulya.criminalintent.models.CrimeListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = ">>> CrimeListFragment"

class CrimeListFragment : Fragment(), MenuProvider {

    private val crimeListViewModel: CrimeListViewModel by viewModels()


    private var _binding: FragmentCrimeListBinding? = null  // backing property for binding
    private val binding
        get() = checkNotNull(_binding) {
            "binding is null. Check if the view is visible"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            /**
             * [repeatOnLifecycle]:

             * implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.1'

             * will begin executing your coroutine code when your fragment enters
             * the started state and will continue running in the resumed state.
             * But if your app is backgrounded and your fragment is no longer visible,
             * repeatOnLifecycle(…) will cancel the work once the fragment falls from
             * the started state to the created state.
             * If your lifecycle re-enters the started state without fully being destroyed,
             * your coroutine will be restarted from the beginning, repeating its work.
             */


            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                crimeListViewModel.crimes.collect { crimes ->

                    Log.d(TAG, "onViewCreated: ${crimes.isEmpty()}")
                    /**
                     * When the crimes is empty then we can show a msg
                     * that recycler view is empty
                     */
                    if(crimes.isEmpty()){
                        binding.msgText.visibility = View.VISIBLE
                    }
                    else {
                        binding.msgText.visibility = View.INVISIBLE
                    }

                    val adapter = CrimeListAdapter(crimes){ crimeId ->
                        findNavController().navigate(
                            CrimeListFragmentDirections.showCrimeDetail(crimeId)
                        )
                    }
                    binding.crimeRecyclerView.adapter = adapter
                }

            }

        }
    }


    override fun onDestroyView() {
        /**
         * We make _binding = null to free resource
         * by destroying all reference to the view
         * of the fragment when its not visible.
         *
         * If we don't do that, then the system sees that
         * there is a chance you might access the view later
         * and prevents the system from clearing its memory.
         */
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        Log.d(TAG, "onMenuItemSelected: ${menuItem.itemId}")
        Log.d(TAG, "onMenuItemSelected: ${R.menu.fragment_crime_list}")

        return when(menuItem.itemId){
            R.id.new_crime -> {
                Log.d(TAG, "onMenuItemSelected: ${menuItem.itemId}")
                showNewCrime()
                true
            }

            else -> false
        }
    }

    private fun showNewCrime() {
        val newCrime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = false
        )

        viewLifecycleOwner.lifecycleScope.launch{
            crimeListViewModel.addNewCrime(newCrime)

            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(newCrime.id)
            )
        }
    }
}