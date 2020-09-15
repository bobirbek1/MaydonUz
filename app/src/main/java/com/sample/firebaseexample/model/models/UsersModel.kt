package com.sample.firebaseexample.model.models

import java.util.*

data class UsersSimpleData(
    val name: String,
    val surname:String,
    val phone: String
)

data class UsersFullData(
    val birthday: Date,
    val gender: String,
    val profileImagePath: String
)

data class Favourites(
    val docPath:String,
    val docRef:String
)
data class BuyurtmalarTarixi(
    val maydonId:Long,
    val maydonUId:String,
    val buyurtmaSummasi:Long,
    val buyurtmaBoshlanishVaqti:Date,
    val buyurtmaTugashiVaqti:Date
)

