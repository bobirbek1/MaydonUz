package com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.models.ModelSharhlar
import kotlinx.android.synthetic.main.item_comments.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterComments(private val context: Context,private val list: ArrayList<ModelSharhlar>): RecyclerView.Adapter<AdapterComments.VH>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH{
        val view = LayoutInflater.from(context).inflate(R.layout.item_comments,parent,false)
        return VH(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        return holder.onBind(list[position])
    }

    class VH(view:View):RecyclerView.ViewHolder(view){


        fun onBind(model:ModelSharhlar){
            val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            itemView.ratingbar_comments.rating = model.bahosi.toFloat()
            itemView.date_comment.setText(format.format(model.date!!.toDate()))
            itemView.tv_comment.text = model.sharh
        }

    }

}