package com.sample.firebaseexample.ui.main_screen.ui.home.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import com.sample.firebaseexample.model.models.ModelNews
import kotlinx.android.synthetic.main.item_news.view.*
import kotlin.collections.ArrayList

class Adapter(private val models: ArrayList<ModelNews>, private val context: Context,private var itemClickListener : ((ModelNews) -> Unit)) :
    RecyclerView.Adapter<Adapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
val view = LayoutInflater.from(context).inflate(R.layout.item_news,parent,false)
        return VH(view)
    }

    override fun getItemCount(): Int = models.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(models[position],itemClickListener,context)
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        private var callFirebaseStorage: CallFirebaseStorage = CallFirebaseStorage()

        @TargetApi(Build.VERSION_CODES.N)
        fun onBind(
            models: ModelNews,
            itemClickListener: (ModelNews) -> Unit,
            context: Context
        ) {
            callFirebaseStorage.downloadNewsImages(models.image,itemView.iv_news,context)
            itemView.tv_news_title.text = models.title
            itemView.tv_news_date.text = models.date?.toDate().toString()
            itemView.setOnClickListener {
                itemClickListener.invoke(models)
            }
        }
    }
}