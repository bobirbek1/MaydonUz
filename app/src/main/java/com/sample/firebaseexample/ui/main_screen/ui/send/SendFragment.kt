package com.sample.firebaseexample.ui.main_screen.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.sample.firebaseexample.R
import kotlinx.android.synthetic.main.app_bar_main_screen.*
import kotlinx.android.synthetic.main.content_main_screen.*

class SendFragment : Fragment(), View.OnClickListener {
private lateinit var rootView: View
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         rootView = inflater.inflate(R.layout.fragment_send, container, false)
//        toolbar = activity!!.toolbar
//        toolbar.setNavigationIcon(R.drawable.ic_back)
        activity!!.bottom_navigation.visibility = View.GONE
//        toolbar.setNavigationOnClickListener(this)
        return rootView
    }

    override fun onClick(v: View?) {
        activity!!.onBackPressed()
//        toolbar.setNavigationIcon(R.drawable.ic_menu)
    }
}