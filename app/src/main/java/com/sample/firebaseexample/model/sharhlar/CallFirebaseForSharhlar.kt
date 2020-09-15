package com.sample.firebaseexample.model.sharhlar

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.firebaseexample.model.models.ModelSharhlar
import java.util.*

class CallFirebaseForSharhlar {
    fun saveSharhlar(bahosi: Int, sharh: String,docPath:String,uid:String) {
        val firebase = FirebaseFirestore.getInstance().document(docPath).collection("sharhlar")
//        val id = firebase
//            .document()
//            .id
        val model = ModelSharhlar(uid,bahosi, Timestamp(Date()),sharh,"$docPath/sharhlar/$uid")
        firebase
            .document(uid)
            .set(model)
            .addOnSuccessListener {
                Log.d("saveSharhlar","Sharhlar saved success")
            }
            .addOnFailureListener {
                Log.e("saveSharhlar",it.message!!)
            }
    }
}