package com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import kotlinx.android.synthetic.main.item_images.view.*

class ImageAdapter(private val context: Context, private val list: List<String>) : RecyclerView.Adapter<ImageAdapter.VH>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
   val view = LayoutInflater.from(context).inflate(R.layout.item_images,parent,false)
        return VH(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    inner class VH(view: View):RecyclerView.ViewHolder(view){

        fun onBind(path : String){
            val callFirebaseStorage = CallFirebaseStorage()
        callFirebaseStorage.downloadNewsImages(path,itemView.iv_all_maydon,context)
        }
    }

}