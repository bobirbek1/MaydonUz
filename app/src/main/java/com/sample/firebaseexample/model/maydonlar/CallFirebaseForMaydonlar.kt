package com.sample.firebaseexample.model.maydonlar

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.firebaseexample.model.models.ModelMaydon
import com.sample.firebaseexample.model.models.ModelNews
import com.sample.firebaseexample.model.models.ModelSharhlar
import com.sample.firebaseexample.ui.main_screen.ui.home.CallbackNews

class CallFirebaseForMaydonlar {
    private val firestore = FirebaseFirestore.getInstance()

    fun getMaydonlar(callback: ((ArrayList<ModelMaydon>) -> Unit)) {
        val maydonlarList = arrayListOf<ModelMaydon>()
        firestore
            .collection("maydonlar")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    maydonlarList.add(document.toObject(ModelMaydon::class.java))
                }
                callback.invoke(maydonlarList)
            }
            .addOnFailureListener {
                Log.e("getData", it.message!!)
            }
        Log.d("getData:", "$maydonlarList")
    }

    fun updateMaydonlarCountOfViews(docRef: String, countViews: Int) {
        val countViews1 = countViews + 1
        firestore
            .document(docRef)
            .update("countOfViews",countViews1)
            .addOnSuccessListener {
                Log.d("updateNewsCountOfViews","update views successful")
            }
            .addOnFailureListener {
                Log.w("updateNewsCountOfViews", "update updated unsucsessfully", it)
            }
    }

    fun getMaydonById(docRef: String,callback: ((ModelMaydon) -> Unit)) {

        firestore
            .document(docRef)
            .get()
            .addOnSuccessListener {
                if (it.exists()){
                    callback.invoke(it.toObject(ModelMaydon::class.java)!!) }
            }
            .addOnFailureListener {
                Log.w("getNewsById", "getting news by id was unsuccess", it)
            }
    }
    fun getComments(docPath:String,callback: (ArrayList<ModelSharhlar>) -> Unit){
        val listcomments = arrayListOf<ModelSharhlar>()
        firestore
            .document(docPath)
            .collection("sharhlar")
            .orderBy("date")
            .get()
            .addOnSuccessListener {
                for (document in it){
                    listcomments.add(document.toObject(ModelSharhlar::class.java))
                }
                callback.invoke(listcomments)
            }
            .addOnFailureListener {
                Log.e("getComments",it.message!!)
            }
    }
//    fun checkForComment(uid:String,docPath: String,callback:((ModelSharhlar?) -> Unit)){
//        firestore.document(docPath)
//            .collection("sharhlar")
//            .get()
//            .addOnSuccessListener {
//                for (document in it){
//                    if (document["uid"] == uid){
//                        callback.invoke(document.toObject(ModelSharhlar::class.java))
//                    } else {
//                        callback.invoke(null)
//                    }
//                }
//            }
//            .addOnFailureListener {
//
//            }
//    }
}