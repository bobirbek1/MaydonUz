package com.sample.firebaseexample.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.firebaseexample.model.models.BuyurtmalarTarixi
import com.sample.firebaseexample.model.models.Favourites
import com.sample.firebaseexample.model.models.UsersFullData
import com.sample.firebaseexample.model.models.UsersSimpleData
import com.sample.firebaseexample.ui.main_screen.ui.profil.ModelProfil

const val USERS = "Users"
const val PROFIL = "Profil"
const val TARIX = "Tarix"
const val FULLDATA = "Full data"
const val FAVOURITES = "favourites"
const val SIMPLEDATA = "simple data"
//const val KEYNAME = "name"
//const val KEYSURNAME = "Surname"
//const val KEYPHONE = "phone"
//const val KEYBIRTHDAY = "birthday"
//const val KEYGENDER = "gender"
//const val KEYIMAGEPATH = "image path"
//const val KEYID = "id"
//const val KEYUUID = "uuid"

//class CallFirebaseFirestore {
//    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//
//    private val noteRef = db.collection(USERS).document("${firebaseAuth.currentUser}")
//
//    fun getProfilSimpleData(): UsersSimpleData? {
//
//        var userSimpleModel: UsersSimpleData? = null
//        noteRef.collection(PROFIL).document(SIMPLEDATA)
//            .get()
//            .addOnSuccessListener {
//                userSimpleModel = it.toObject(UsersSimpleData::class.java)
//            }
//            .addOnFailureListener { exception ->
//                Log.w("getProfilSimpleData", "Error getting documents.", exception)
//            }
//        return userSimpleModel
//    }
//
//    fun getProfilFullData(): UsersFullData? {
//        var userFullData: UsersFullData? = null
//        noteRef.collection(PROFIL).document(FULLDATA)
//            .get()
//            .addOnSuccessListener {
//                userFullData = it.toObject(UsersFullData::class.java)
//            }
//            .addOnFailureListener {
//                Log.w("getProfilFullData", "Error getting documents", it)
//            }
//        return userFullData
//
//    }
//
//    fun getProfilFavourites(): Favourites? {
//        var favourites: Favourites? = null
//        noteRef.collection(PROFIL).document(FAVOURITES)
//            .get()
//            .addOnSuccessListener {
//                favourites = it.toObject(Favourites::class.java)
//            }
//            .addOnFailureListener {
//                Log.w("getProfilFullData", "Error getting documents", it)
//            }
//        return favourites
//
//    }
//    fun getTarix(): BuyurtmalarTarixi? {
//        var tarix: BuyurtmalarTarixi? = null
//        noteRef.collection(PROFIL).document(TARIX)
//            .get()
//            .addOnSuccessListener {
//                tarix = it.toObject(BuyurtmalarTarixi::class.java)
//            }
//            .addOnFailureListener {
//                Log.w("getProfilFullData", "Error getting documents", it)
//            }
//        return tarix
//    }
//    fun setSimpleData(simpleData: UsersSimpleData): Boolean {
//        var isSuccessFull = false
//        noteRef.collection(PROFIL).document(SIMPLEDATA)
//            .set(simpleData)
//            .addOnSuccessListener {
//                isSuccessFull = true
//            }
//            .addOnFailureListener {
//                Log.e("setSimpleData",it.message!!)
//            }
//        return isSuccessFull
//    }
//    fun setFullData(fullData: ModelProfil): Boolean {
//        var isSuccessFull = false
//        noteRef.collection(PROFIL).document(FULLDATA)
//            .set(fullData)
//            .addOnSuccessListener {
//                isSuccessFull = true
//            }
//            .addOnFailureListener {
//                Log.e("setFullData",it.message!!)
//            }
//        return isSuccessFull
//    }
//    fun setFavourites(favourites: Favourites): Boolean {
//        var isSuccessFull = false
//        noteRef.collection(PROFIL).document(FULLDATA)
//            .set(favourites)
//            .addOnSuccessListener {
//                isSuccessFull = true
//            }
//            .addOnFailureListener {
//                Log.e("setFullData",it.message!!)
//            }
//        return isSuccessFull
//    }
//    fun setTarix(tarixi: BuyurtmalarTarixi): Boolean {
//        var isSuccessFull = false
//        noteRef.collection(PROFIL).document(FULLDATA)
//            .set(tarixi)
//            .addOnSuccessListener {
//                isSuccessFull = true
//            }
//            .addOnFailureListener {
//                Log.e("setFullData",it.message!!)
//            }
//        return isSuccessFull
//    }
//    fun SetFavourites(docPath:String,name:String,callback: (Boolean) -> Unit){
//        val docRef = "$USERS/${firebaseAuth.currentUser?.uid}/favourites/name"
//        val model = Favourites(docPath,docRef)
//        noteRef.collection("favourites")
//            .document(name)
//            .set(model)
//            .addOnSuccessListener {
//                Log.d("setFavourites","favourites set successful")
//                callback.invoke(true)
//            }
//            .addOnFailureListener {
//                callback.invoke(false)
//                Log.e("setFavourites",it.message!!)
//            }
//    }
//    fun GetFavourites(callback:((ArrayList<Favourites>) -> Unit)){
//        val list  = arrayListOf<Favourites>()
//        noteRef
//            .collection("favourites")
//            .get()
//            .addOnSuccessListener {
//                for (document in it){
//                   list.add(document.toObject(Favourites::class.java))
//                }
//                callback.invoke(list)
//            }
//            .addOnFailureListener {
//                Log.e("getFavourites",it.message!!)
//            }
//    }
//    fun DeleteFromFavourites(docRef:String,callback: (Boolean) -> Unit){
//        db.document(docRef)
//            .delete()
//            .addOnSuccessListener {
//                callback.invoke(true)
//            }
//            .addOnFailureListener {
//                callback.invoke(false)
//                Log.e("deleteFromFavourites",it.message!!)
//            }
//    }
//}