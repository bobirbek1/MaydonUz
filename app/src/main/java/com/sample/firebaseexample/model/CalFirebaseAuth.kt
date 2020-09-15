package com.sample.firebaseexample.model

import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.sample.firebaseexample.fragments.PHONE
import java.util.concurrent.TimeUnit

const val MYPREF = "MyPref"
const val ISVERIFIED = "isVerified"

class CalFirebaseAuth(val context: Context,val preference_key:String) {

    private val auth = FirebaseAuth.getInstance()

    fun isUserAuthentifcated(): Boolean {
        val prefs =
            context.getSharedPreferences(preference_key, Context.MODE_PRIVATE)
        val mPhone = prefs.getString(PHONE, "")
        val phone = auth.currentUser?.phoneNumber
        return mPhone == phone
    }


    fun sendVerificationCodeWithoutToken(
        phone: String?,
        stateChangedCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone!!, 30L,
            TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, stateChangedCallback
        )
    }

    fun sendVerificationCodeWithToken(
        phone: String?,
        token: PhoneAuthProvider.ForceResendingToken,
        stateChangedCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone!!, 30L,
            TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, stateChangedCallback,
            token
        )
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,onCompleteListener: OnCompleteListener<AuthResult>) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(onCompleteListener)
    }


}