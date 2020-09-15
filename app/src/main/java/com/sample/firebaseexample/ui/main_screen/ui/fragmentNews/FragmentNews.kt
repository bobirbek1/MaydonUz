package com.sample.firebaseexample.ui.main_screen.ui.fragmentNews


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import com.sample.firebaseexample.model.models.ModelNews
import com.sample.firebaseexample.model.news.CallFirebaseForNews
import kotlinx.android.synthetic.main.content_main_screen.*
import kotlinx.android.synthetic.main.fragment_news.view.*
import java.text.SimpleDateFormat
import java.util.*


class FragmentNews : Fragment(), (ModelNews) -> Unit, View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var callFirebaseForNews: CallFirebaseForNews
    private lateinit var callFirebaseStorage: CallFirebaseStorage
    private lateinit var gson: Gson


    private var document: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        callFirebaseForNews = CallFirebaseForNews()
        callFirebaseStorage  = CallFirebaseStorage()
        gson = Gson()
        rootView = inflater.inflate(R.layout.fragment_news, container, false)
        document = arguments?.getString("document")
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setData(document)
        activity!!.bottom_navigation.visibility = View.GONE
        rootView.toolbar_news.setNavigationOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(document: String?) {
        val type  =object :TypeToken<ModelNews>(){}.type
        val modelNews:ModelNews = gson.fromJson(document,type)
        val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        callFirebaseStorage.downloadNewsImages(modelNews.image,rootView.news_image,context!!)
        rootView.news_title.text = modelNews.title
        rootView.news_date.text = "${formatTime.format(modelNews.date!!.toDate())} / ${format.format(modelNews.date.toDate())}"
        rootView.news_count_views.text = modelNews.countOfViews.toString()
        rootView.news_body.text = modelNews.body
    }

    override fun invoke(modelNews: ModelNews) {

    }

    override fun onClick(v: View?) {
        activity!!.onBackPressed()
    }


}
