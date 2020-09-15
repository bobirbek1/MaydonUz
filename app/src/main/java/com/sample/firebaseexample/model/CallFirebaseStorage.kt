package com.sample.firebaseexample.model

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class CallFirebaseStorage {

    private val refUsersStroge = FirebaseStorage.getInstance().reference.child("Users/${FirebaseAuth.getInstance().currentUser?.uid}/")


    fun downloadNewsImages(path:String, view: ImageView, context:Context){
        val refNews = FirebaseStorage.getInstance().getReferenceFromUrl(path)

        refNews.downloadUrl.addOnSuccessListener {
            Glide.with(context)
                .load(it)
                .into(view)
        }
            .addOnFailureListener {
                Toast.makeText(context,"some problem to get download url",Toast.LENGTH_SHORT).show()
                Log.e("get download url",it.message!!)
            }

    }
    fun downloadMaydonImages(path: ArrayList<String>,view: ImageView,context: Context){
        (0..path.size).forEach {
            val refMaydonlar = FirebaseStorage.getInstance().getReferenceFromUrl(path[it])

        }


    }
//    fun saveImage(){
//    refUsers.
//    }
//    fun getImage(){
//
//    }

}