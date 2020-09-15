package com.sample.firebaseexample.contract

import androidx.lifecycle.LiveData
import com.sample.firebaseexample.model.models.BuyurtmalarTarixi
import com.sample.firebaseexample.model.models.Favourites
import com.sample.firebaseexample.model.models.UsersFullData
import com.sample.firebaseexample.model.models.UsersSimpleData

interface FirebaseContract {

    interface Model{
        fun getProfilSimoleData():LiveData<UsersSimpleData>
        fun getProfilFullData():LiveData<UsersFullData>
        fun getProfilFavourites(): LiveData<Favourites>
        fun getTarix(): LiveData<BuyurtmalarTarixi>
        fun setProfilSimpleData(data: UsersSimpleData): Boolean
        fun setProfilFullData(data: UsersFullData):Boolean
        fun setFavourites(data: Favourites): Boolean
        fun setTarix(data: BuyurtmalarTarixi): Boolean
    }
    interface View{
        fun showProggresBar()
        fun hideProgressBar()
        fun onSuccessListener()
        fun onFailureListener()
    }
    interface Presenter{

    }
}