package com.sample.firebaseexample

import android.app.Application
import android.content.Context
import com.sample.firebaseexample.ui.ISVERIFIED

class App : Application(){
    override fun onCreate() {

        val prefs = applicationContext.getSharedPreferences(resources.getString(R.string.shared_preference_key),Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(ISVERIFIED,false)
        editor.apply()
        super.onCreate()
    }
}