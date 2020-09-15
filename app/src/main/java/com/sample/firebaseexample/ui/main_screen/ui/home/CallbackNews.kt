package com.sample.firebaseexample.ui.main_screen.ui.home

import android.media.Image
import com.sample.firebaseexample.model.models.ModelNews

interface CallbackNews {
    fun getNews(response:ArrayList<ModelNews>)
    fun updateNews(response:Boolean)
}