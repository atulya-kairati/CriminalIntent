package com.atulya.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.atulya.criminalintent.fragments.CrimeDetailFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val fragment = CrimeDetailFragment()
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.fragment_container, fragment)
//            .commit()
    }
}