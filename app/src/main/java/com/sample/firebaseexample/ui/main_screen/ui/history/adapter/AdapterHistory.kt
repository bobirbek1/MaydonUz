package com.sample.firebaseexample.ui.main_screen.ui.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.firebaseexample.R

class AdapterHistory(private val list: ArrayList<Int>, private val context: Context) :
    RecyclerView.Adapter<AdapterHistory.VH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_history, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list.get(position))
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(list: Int) {

        }
    }
}
