package com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.maydonlar.CallFirebaseForMaydonlar
import com.sample.firebaseexample.model.models.ModelMaydon
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter.Adapter
import kotlinx.android.synthetic.main.content_main_screen.*
import kotlinx.android.synthetic.main.fragment_list_maydonlar.view.*


const val LIST_MAYDONLAR = "listMaydonlar"

class FragmentListMaydonlar : Fragment(), (ArrayList<ModelMaydon>) -> Unit,
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var rootView: View
    private lateinit var callFirebaseForMaydonlar: CallFirebaseForMaydonlar
    private lateinit var preferences: SharedPreferences
    private lateinit var listMaydon: ArrayList<ModelMaydon>
    private lateinit var gson: Gson
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity!!.bottom_navigation.visibility = View.VISIBLE
        rootView = inflater.inflate(R.layout.fragment_list_maydonlar, container, false)
        preferences = activity?.getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )!!
        gson = Gson()
        callFirebaseForMaydonlar = CallFirebaseForMaydonlar()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        rootView.srl_list_maydonlar.setOnRefreshListener(this)
        rootView.srl_list_maydonlar.setColorSchemeResources(R.color.colorAqua,R.color.colorOrange,R.color.colorStarFilled)
        val json = preferences.getString(LIST_MAYDONLAR, "")
        if (json!!.isNotEmpty()) {
            val type = object : TypeToken<ArrayList<ModelMaydon>>() {}.type
            listMaydon = gson.fromJson(json, type)
            setRv(listMaydon)
        } else {
            callFirebaseForMaydonlar.getMaydonlar(this)
        }
    }

    override fun invoke(response: ArrayList<ModelMaydon>) {
        listMaydon = response
        Log.d("getMaydonData", response.toString())
        val json = gson.toJson(response)
        preferences.edit()
            .putString(LIST_MAYDONLAR, json)
            .apply()
        setRv(listMaydon)
        rootView.srl_list_maydonlar.isRefreshing = false
    }

    private fun setRv(list: ArrayList<ModelMaydon>) {
        val itemClickListener: ((String, Int) -> Unit) = { docPath, views ->
            updateViews(docPath, views)
            findNavController().navigate(R.id.fragmentMaydon, bundleOf("docPath" to docPath))
        }
        val favouriteClickListener: ((String) -> Unit) = {

        }
        val adapter = Adapter(context!!, list, itemClickListener,favouriteClickListener)
        rootView.rv_maydonlar.adapter = adapter
        setFilter(adapter)
    }

    private fun setFilter(adapter: Adapter) {
        val editText = rootView.et_search_maydonlar
        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            adapter.filter.filter(s.toString())
            }
        })
    }


    private fun updateViews(docPath: String, views: Int) {
        callFirebaseForMaydonlar.updateMaydonlarCountOfViews(docPath, views)
    }

    override fun onRefresh() {
        callFirebaseForMaydonlar.getMaydonlar(this)
    }

}