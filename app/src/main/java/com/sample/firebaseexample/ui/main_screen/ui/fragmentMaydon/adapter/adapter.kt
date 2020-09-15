package com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import com.sample.firebaseexample.model.models.ModelMaydon
import kotlinx.android.synthetic.main.item_list_mayon.view.*
import java.util.*
import kotlin.collections.ArrayList

class Adapter(
    private val context: Context,
    list: ArrayList<ModelMaydon>,
    private val listener: (String, Int) -> Unit,
    private val favouriteListener: (String) -> Unit
) : RecyclerView.Adapter<Adapter.VH>(),Filterable {

    private var listMaydonlar:ArrayList<ModelMaydon> = list
    private var listMaydonlarFull:ArrayList<ModelMaydon> = ArrayList(listMaydonlar)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_mayon, parent, false)
        return VH(view)
    }

    override fun getItemCount() = listMaydonlar.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(listMaydonlar[position], listener, context)
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
private val callFirebaseStorage = CallFirebaseStorage()

        fun onBind(model: ModelMaydon, listener: ((String,Int) -> Unit), context: Context) {
            callFirebaseStorage.downloadNewsImages(model.imagesPath[0],itemView.iv_list_maydonlar,context)
            itemView.tv_list_maydonlar_nomi.text = model.nomi
            itemView.list_maydonlar_korilganlari.text = model.countOfViews.toString()
            itemView.list_maydonlar_bahosi.text = model.bahosi.toString()
            itemView.list_maydonlar_manzili.text = model.address
            itemView.list_maydonlar_ratingbar.rating = model.bahosi

            itemView.ll_list_maydonlar.setOnClickListener {
                listener.invoke(model.docPath,model.countOfViews)
            }
            itemView.iv_favourite.setOnClickListener {
                itemView.iv_favourite.setImageResource(R.drawable.ic_heart_filled)
                favouriteListener.invoke(model.docPath)
            }
        }

    }

    override fun getFilter(): Filter {
return filter()
    }
    private fun filter() = object :Filter(){
        val filteredList = arrayListOf<ModelMaydon>()
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            filteredList.clear()
            if (constraint == null || constraint.isEmpty()){
                filteredList.addAll(listMaydonlarFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()
                for (model in listMaydonlarFull) {
                    if (model.nomi.toLowerCase(Locale.getDefault()).contains(filterPattern)){
                        filteredList.add(model)
                    }
                }
            }
            val results = FilterResults()
            return  results.also {
                it.values = filteredList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {                                               
            listMaydonlar.clear()
            listMaydonlar.addAll(results?.values as Collection<ModelMaydon>)
            notifyDataSetChanged()
        }
    }

}