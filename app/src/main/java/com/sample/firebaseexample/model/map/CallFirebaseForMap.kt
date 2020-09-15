package com.sample.firebaseexample.model.map

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sample.firebaseexample.ui.main_screen.ui.mapFragment.CallbackMap


class CallFirebaseForMap() {
    private val firestoreAuth = FirebaseFirestore.getInstance().collection("maydonlar")

    fun getData(callback: CallbackMap){
        val locationList = mutableListOf<LatLng>()
//        val documentId = getDocumentId()
        val list =  firestoreAuth
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val geoPoint = document.getGeoPoint("manzili")
                    Log.d("getData:", "$geoPoint")
                    locationList.add(LatLng(geoPoint!!.latitude, geoPoint.longitude))
                }
                callback.getDataCallback(locationList)
            }
            .addOnFailureListener {
                Log.e("getData", it.message!!)
            }
        Log.d("getData:","$locationList")

    }
//
//    private fun getDocumentId(): ArrayList<String> {
//        val documentId = arrayListOf<String>()
//
//        firestoreAuth
//            .get()
//            .addOnSuccessListener {
//                Log.d("getDocument","documents:$it")
//                for (document in it) {
//                    documentId.add(document.id)
//                }
//            }
//            .addOnFailureListener {
//                Log.e("getDocumentId", it.message!!)
//            }
//        return documentId
//    }

//    fun setData(geoPoint: GeoPoint, callback: CallbackMap) {
//        firestoreAuth
//            .document()
//            .set(
//                hashMapOf(
//                    "manzili" to geoPoint
//                )
//            )
//            .addOnSuccessListener {
//                Log.d("setData", "Data saved successfully")
//                callback.setDataCallback(true)
//            }
//            .addOnFailureListener {
//                callback.setDataCallback(false)
//                Log.e("setData", it.message!!)
//            }
//
//    }

    fun deleteData(geoPoint: GeoPoint, callback: CallbackMap) {
//        var listId = arrayListOf<String>()
//        firestoreAuth.document()
//        val i = firestoreAuth
//            .whereEqualTo("manzili", geoPoint)
//            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                if (querySnapshot != null && firebaseFirestoreException == null) {
//                    for (i in querySnapshot.documents) {
//                        Log.d("documentId:", i.id)
//                        listId.add(i.id)
//                    }
//                } else if (firebaseFirestoreException != null) {
//                    Log.e("deleteData", firebaseFirestoreException.message!!)
//                } else {
//                    Log.e("deleteData", "we have some problems")
//                }
//            }
//        for (path in listId) {
//            firestoreAuth.document(path)
//                .delete()
//                .addOnSuccessListener {
//                    callback.deleteDataCallback(true)
//                    Log.d("deleteData", "document deleted successfull")
//                }
//                .addOnFailureListener {
//                    callback.deleteDataCallback(false)
//                    Log.w("deleteData", "we have exeption", it)
//                }
//        }

    }
}
