package com.sample.firebaseexample.fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sample.firebaseexample.R
import kotlinx.android.synthetic.main.registiration_fragment.*
import kotlinx.android.synthetic.main.registiration_fragment.view.*

const val USERKEY = "userKey"
const val USERNAME = "userName"
const val USERSURNAME = "surname"
const val PHONE = "phone"
class RegistrationFragment:Fragment(), View.OnClickListener {

    private lateinit var auth:FirebaseAuth
private val list = arrayOf("+998","+996","+79")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val rootView = inflater.inflate(R.layout.registiration_fragment,container,false)
        rootView.spinner.adapter = ArrayAdapter<String>(activity!!,android.R.layout.simple_spinner_dropdown_item,list)
        auth = FirebaseAuth.getInstance()
        rootView.btn_reg.setOnClickListener(this)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressed()
    }

    private fun getNames() {
        val name = name.text.toString()
        val surname = surname.text.toString()
        val prefs = context?.applicationContext?.getSharedPreferences(USERKEY,Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString(USERNAME,name)
        editor?.putString(USERSURNAME,surname)
        editor?.apply()
    }
    override fun onClick(p0: View?) {
val code = list[spinner.selectedItemPosition]
        val number = et_number.text.toString()
        if (number.isEmpty()){
            et_number.setError("number is required")
            et_number.requestFocus()
            return
        } else if (number.length != 9){
            et_number.setError("number length must be 9")
            et_number.requestFocus()
            return
        }
        val phone = code + number
        val prefs = context?.applicationContext?.getSharedPreferences(resources.getString(R.string.shared_preference_key),Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString(PHONE,phone)
        editor?.apply()
        openVerifyFargment(phone)
    }
    private fun openVerifyFargment(phone:String){
        val bundle = Bundle()
        bundle.putString("phone",phone)
findNavController().navigate(R.id.action_registrationFragment2_to_verificationFragment,bundle)
    }
fun onBackPressed(){
    activity?.finish()
}
}