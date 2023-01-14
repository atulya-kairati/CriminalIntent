package com.atulya.criminalintent

import android.app.Application
import android.util.Log

class CriminalIntentApplication: Application() {

    /**
     * To do work as soon as your application is ready,
     * you can create an Application subclass. This allows
     * you to access lifecycle information about the application itself.
     *
     * Create a class called CriminalIntentApplication that extends Application,
     * and override Application.onCreate() to set up the repository initialization.
     *
     * In AndroidManifest.xml
     * <application
     *      android:name=".CriminalIntentApplication"
     */

    override fun onCreate() {

        /**
         * Application.onCreate() is called by the system when your
         * application is first loaded into memory.
         * What makes it different is the fact that your
         * CriminalIntentApplication is not re-created on configuration changes.
         * It is created when the app launches and destroyed when your app
         * process is destroyed. That makes it a good place to do any kind
         * of one-time initialization operations.
         *
         * But in order for your application class to be used by the system,
         * you need to register it in your manifest.
         */

        super.onCreate()
        CrimeRepository.initialize(this)

        Log.d(">>>", " sss")
    }
}