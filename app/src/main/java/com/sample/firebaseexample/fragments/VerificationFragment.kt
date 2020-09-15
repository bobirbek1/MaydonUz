package com.sample.firebaseexample.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.sample.firebaseexample.ui.ISVERIFIED
import com.sample.firebaseexample.R
import kotlinx.android.synthetic.main.verification_fragment.*
import kotlinx.android.synthetic.main.verification_fragment.view.*
import java.util.concurrent.TimeUnit

class VerificationFragment : Fragment(), View.OnClickListener {


    private lateinit var rootView: View
    private  var phone:String? = null
    private lateinit var auth:FirebaseAuth
    private var verificationid:String? = null
    private var token:PhoneAuthProvider.ForceResendingToken? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        phone = arguments?.getString("phone"," ")
        auth = FirebaseAuth.getInstance()
        rootView = inflater.inflate(R.layout.verification_fragment, container, false)
        rootView.btn_verify.setOnClickListener(this)
        rootView.tv_resend.setOnClickListener(this)
        sendVerificationCode(phone)
        return rootView
    }

    private fun  verifyCode(code: String){
        if (verificationid != null){
        val credential = PhoneAuthProvider.getCredential(verificationid!!,code)
        signInWithPhoneAuthCredential(credential)}
        else{
            rootView.et_verify_code.setError("some error")
            rootView.progress.visibility = View.GONE
        }

    }

    private fun sendVerificationCode(phone: String?,token:PhoneAuthProvider.ForceResendingToken? = null) {
        if (token == null){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone!!,30L,
            TimeUnit.SECONDS,TaskExecutors.MAIN_THREAD,object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    val code = credential.smsCode
                    Log.d("verifyPhoneNumber","onVerification completed: $credential")
                    if (code != null) {
                        rootView.progress.visibility = View.VISIBLE
                        rootView.et_verify_code.setText(code)
                        verifyCode(code)
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.d("verifyPhoneAuth","verification failed ${e.message}")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@VerificationFragment.verificationid = verificationId
                    this@VerificationFragment.token = token
                }

            })} else{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone!!,30L,
                TimeUnit.SECONDS,TaskExecutors.MAIN_THREAD,object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        val code = credential.smsCode
                        Log.d("verifyPhoneNumber","onVerification completed: $credential")
                        if (code != null) {
                            rootView.progress.visibility = View.VISIBLE
                            rootView.et_verify_code.setText(code)
                            Toast.makeText(context,"verification code sent sucsessful!",Toast.LENGTH_SHORT).show()
                            verifyCode(code)
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {

                        Log.d("verifyPhoneAuth","verification failed ${e.message}")
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        Log.d("verificationId",verificationId)
                        this@VerificationFragment.verificationid = verificationId
                        this@VerificationFragment.token = token
                    }

                },token)
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                override fun onComplete(result: Task<AuthResult>) {
                    if (result.isSuccessful){
                        savePrefs(true)
                       findNavController().navigate(R.id.action_verificationFragment_to_uploadFragment)
                    } else{
                        Toast.makeText(context,"some error ${result.exception?.message}",Toast.LENGTH_SHORT).show()
                        rootView.progress.visibility = View.GONE
                        auth.currentUser?.delete()
                    }
                }


            })
    }
    fun savePrefs(isVerify: Boolean?) {
        val prefs = context?.applicationContext?.getSharedPreferences(resources.getString(R.string.shared_preference_key), Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        if (isVerify != null)
            editor?.putBoolean(ISVERIFIED, isVerify)
        else
            editor?.putBoolean(ISVERIFIED, false)
        editor?.apply()

    }
    override fun onClick(item: View?) {
        if (item == btn_verify){

       val code =  rootView.et_verify_code.text.toString()
        if (code != "" && !(code.length < 6)){
rootView.progress.visibility = View.VISIBLE
        verifyCode(code)
    } else{
            rootView.et_verify_code.setError("Enter code true...")
            rootView.et_verify_code.requestFocus()
            return
        }
    } else {
            Log.d("onClick","$token")
            if (token != null)
            sendVerificationCode(phone,token)
        }
    }

}