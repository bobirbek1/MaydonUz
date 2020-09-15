package com.sample.firebaseexample.ui.main_screen.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.models.ModelNews
import com.sample.firebaseexample.model.news.CallFirebaseForNews
import com.sample.firebaseexample.ui.main_screen.LIST_NEWS
import com.sample.firebaseexample.ui.main_screen.ui.home.adapter.Adapter
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.content_main_screen.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(), View.OnClickListener, CallbackNews,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var rootView: View
    private lateinit var callFirebaseForNews: CallFirebaseForNews
    private lateinit var preferences: SharedPreferences
    private lateinit var listNews: ArrayList<ModelNews>
    private lateinit var gson: Gson
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        activity!!.toolbar.setNavigationOnClickListener(this)
//        activity!!.toolbar.title = "Home"
        callFirebaseForNews = CallFirebaseForNews()
        preferences = requireActivity().getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )
        gson = Gson()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        rootView.srl_news_list.setOnRefreshListener(this)
        rootView.srl_news_list.setColorSchemeResources(
            R.color.colorAqua,
            R.color.colorOrange,
            R.color.colorStarFilled
        )
        val type = object : TypeToken<ArrayList<ModelNews>>() {}.type
        val json = preferences.getString(LIST_NEWS, "")
        if (json != null && json.isNotEmpty()) {
            listNews = gson.fromJson(json, type)
            setRv(listNews)
        } else {
            callFirebaseForNews.getNews(this)
        }

    }

    private fun setRv(listNews: ArrayList<ModelNews>) {
        val itemOnClick: (ModelNews) -> Unit = { model ->
            callFirebaseForNews.updateNewsCountOfViews(model.docRef, model.countOfViews)
            val json = gson.toJson(model)
            val bundle = bundleOf(Pair("document", json))
            findNavController()
                .navigate(R.id.fragmentNews, bundle)
        }
        val adapter = Adapter(listNews, requireContext(), itemOnClick)
        rootView.rv_news.adapter = adapter
        setOnScroll()
        rootView.rv_news.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setOnScroll() {
        rootView.rv_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy < 0) {
                    activity!!.bottom_navigation.visibility = View.GONE
                }
            }
        })
    }

    override fun onClick(v: View?) {
        activity!!.drawer_layout.openDrawer(activity!!.nav_view)
    }

    override fun getNews(response: ArrayList<ModelNews>) {
        listNews = response
        val json = gson.toJson(response)
        preferences.edit()
            .putString(LIST_NEWS, json)
            .apply()
        setRv(response)
        rootView.srl_news_list.isRefreshing = false
    }

    override fun updateNews(response: Boolean) {

    }

    override fun onRefresh() {
        callFirebaseForNews.getNews(this)
    }


//    fun onClick(v: View?) {
//
//        activity?.drawer_layout?.closeDrawer(GravityCompat.START)
//        activity?.bottom_nav_view?.visibility = BottomNavigationView.GONE
//        rootView.findNavController().navigate(R.id.action_nav_home_to_profilFragment)
//    }

}
