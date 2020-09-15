package com.sample.firebaseexample.ui.main_screen.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.sample.firebaseexample.R
import kotlinx.android.synthetic.main.content_main_screen.*

class GalleryFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_gallery, container, false)
//        toolbar = activity!!.toolbar
        activity!!.bottom_navigation.visibility = View.GONE
//        toolbar.setNavigationIcon(R.drawable.ic_back)
        return rootView
    }

    override fun onResume() {
        super.onResume()
//        toolbar.setNavigationOnClickListener{
//            activity!!.onBackPressed()
//            toolbar.setNavigationIcon(R.drawable.ic_menu)
//        }
    }

//    override fun onClick(v: View?) {
//        activity!!.onBackPressed()
//        toolbar.setNavigationIcon(R.drawable.ic_menu)
//    }
}