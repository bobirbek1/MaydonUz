package com.sample.firebaseexample.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sample.firebaseexample.R
import com.sample.firebaseexample.fragments.PHONE


const val ISVERIFIED = "isVerified"
const val PICK_IMAGE_REQUEST = 1001
const val PERMISSION_REQ_CODE = 1002

class MainActivity : AppCompatActivity() {



    private lateinit var navController:NavController

    private lateinit var auth:FirebaseAuth

    private lateinit var firebaseMessaging: FirebaseMessaging

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        navController = findNavController(R.id.navHostFragment)
        checkFilePermission()
        setFragments()
        firebaseInstantId()
firebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.isAutoInitEnabled = true
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    }
//private fun remoteConfig(){
//    val configSettings = FirebaseRemoteConfigSettings.Builder()
//        .setDeveloperModeEnabled(true)
//        .build()
//    mFirebaseRemoteConfig.setConfigSettings(configSettings)
//mFirebaseRemoteConfig.setDefaults(R.xml.xml)
//}

    private fun setFragments() {
        if (!isUser()) {
            navController.navigate(R.id.action_baseFragment2_to_registrationFragment2)
        } else {
            Toast.makeText(this,"this account disabled",Toast.LENGTH_SHORT).show()
        }
    }



    private fun isUser(): Boolean {
        val prefs = applicationContext.getSharedPreferences(resources.getString(R.string.shared_preference_key), Context.MODE_PRIVATE)
        val mPhone =   prefs.getString(PHONE,"")
        val phone = auth.currentUser?.phoneNumber
        return mPhone == phone
    }

    private fun checkFilePermission(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            var permission = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
             permission += this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                this.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQ_CODE
                )

        }else{
            Log.d("request permissions", "you don't need permissions")
        }
    }



    private fun firebaseInstantId(){
    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("firebaseInstantId", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }

            // Get new Instance ID token
            val token = task.result?.token

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("firebaseInstantId", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
}

}
class MyFirebaseService : FirebaseMessagingService(){

override fun onNewToken(token: String) {
    Log.d("MyFirebaseService", "Refreshed token: $token")

    sendRegistrationToServer(token)
}
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }
}


