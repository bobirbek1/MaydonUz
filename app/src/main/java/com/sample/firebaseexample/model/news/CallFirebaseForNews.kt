package com.sample.firebaseexample.model.news

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.firebaseexample.model.models.ModelNews
import com.sample.firebaseexample.ui.main_screen.ui.home.CallbackNews

class CallFirebaseForNews {
    private val firestoreAuth = FirebaseFirestore.getInstance()

    fun getNews(callback: CallbackNews) {
        val newsList = arrayListOf<ModelNews>()
        firestoreAuth
            .collection("Yangiliklar")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    newsList.add(document.toObject(ModelNews::class.java))
                }
                callback.getNews(newsList)
            }
            .addOnFailureListener {
                Log.e("getData", it.message!!)
            }
        Log.d("getData:", "$newsList")

    }

    fun updateNewsCountOfViews(docRef: String, countViews: Int) {
        val countViews1 = countViews + 1
        firestoreAuth
            .document(docRef)
            .update("countOfViews",countViews1)
            .addOnSuccessListener {
                Log.d("updateNewsCountOfViews","update views successful")
            }
            .addOnFailureListener {
                Log.w("updateNewsCountOfViews", "update updated unsucsessfully", it)
            }
    }

    fun getNewsById(docRef: String, callback: ((ModelNews) -> Unit)) {

        firestoreAuth
            .document(docRef)
            .get()
            .addOnSuccessListener {
                if (it.exists()){
                callback.invoke(it.toObject(ModelNews::class.java)!!) }
            }
            .addOnFailureListener {
                Log.w("getNewsById", "getting news by id was unsuccess", it)
            }
    }
}