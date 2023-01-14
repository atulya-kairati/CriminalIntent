package com.atulya.criminalintent.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.atulya.criminalintent.R
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class CrimeDetailFragmentTest {

    private lateinit var scenario: FragmentScenario<CrimeDetailFragment>

    @Before
    fun setUp() {
        scenario = FragmentScenario.launch(CrimeDetailFragment::class.java)

    }

    @Test
    fun checkEditText(){

    }

    @After
    fun tearDown() {
        scenario.close()
    }
}