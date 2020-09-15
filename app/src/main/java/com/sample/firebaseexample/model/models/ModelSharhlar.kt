package com.sample.firebaseexample.model.models

import com.google.firebase.Timestamp

data class ModelSharhlar(
    val UID: String = "",
    val bahosi: Int = 0,
    val date: Timestamp? = null,
    val sharh: String = "",
    val docPath: String = ""
)