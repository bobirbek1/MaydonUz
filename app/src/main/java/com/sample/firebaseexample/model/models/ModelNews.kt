package com.sample.firebaseexample.model.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.local.ReferenceSet

data class ModelNews(
    val id:Int,
    val date: Timestamp?,
    val title:String,
    val body:String,
    val countOfViews:Int,
    val image:String,
    val docRef: String
){
    constructor() : this (0,null,"","",0,"","")
}
data class News(
    val id:Int,
    val date:Timestamp,
    val title:String,
    val countOfViews:Int,
    val image:String
)