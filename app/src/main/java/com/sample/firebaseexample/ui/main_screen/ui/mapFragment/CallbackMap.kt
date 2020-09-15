package com.sample.firebaseexample.ui.main_screen.ui.mapFragment

import com.google.android.gms.maps.model.LatLng
import com.sample.firebaseexample.model.models.ModelMaydon

interface CallbackMap {
    fun getDataCallback(response: MutableList<LatLng>)

}