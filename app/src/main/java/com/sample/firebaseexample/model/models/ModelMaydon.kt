package com.sample.firebaseexample.model.models

import com.google.firebase.firestore.GeoPoint
import kotlin.collections.ArrayList

data class ModelMaydon(
    val docPath: String = "",
    val address: String = "",
    val imagesPath: ArrayList<String> = arrayListOf(),
    val manzili: GeoPoint? = null,
    val moljal: String = "",
    val nomi: String = "",
    val bahosi: Float = 0f,
    val countOfViews: Int = 0,
    val tel:ArrayList<String> = arrayListOf(),
    val countOfComment:Int = 0
)