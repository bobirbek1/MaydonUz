package com.sample.firebaseexample.ui.main_screen.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.sample.firebaseexample.R
import com.sample.firebaseexample.ui.main_screen.ui.history.adapter.AdapterHistory
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.app_bar_main_screen.*
import kotlinx.android.synthetic.main.fragment_favourite.*

class FragmentFavourite : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false)
//        activity!!.toolbar.setNavigationOnClickListener(this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        val list = arrayListOf(1, 2, 3, 4, 5, 6)
        rv_history.adapter = AdapterHistory(list, context!!)
        rv_history.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )
    }

    override fun onClick(v: View?) {
        activity!!.drawer_layout.openDrawer(activity!!.nav_view)
    }

}