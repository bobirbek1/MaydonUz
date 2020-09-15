package com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.CallFirebaseStorage
import com.sample.firebaseexample.model.maydonlar.CallFirebaseForMaydonlar
import com.sample.firebaseexample.model.models.ModelMaydon
import com.sample.firebaseexample.model.models.ModelSharhlar
import com.sample.firebaseexample.ui.main_screen.UID
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter.AdapterComments
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.adapter.ImageAdapter
import kotlinx.android.synthetic.main.content_main_screen.*
import kotlinx.android.synthetic.main.fragment_maydon.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FragmentMaydon : Fragment(), (ModelMaydon) -> Unit, View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var callFirebaseForMaydonlar: CallFirebaseForMaydonlar
    private lateinit var docPath: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listComment: ArrayList<ModelSharhlar>
    private lateinit var gson: Gson
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().bottom_navigation.visibility = View.GONE
        rootView = inflater.inflate(R.layout.fragment_maydon, container, false)
        callFirebaseForMaydonlar.getMaydonById(docPath, this)
        listComment = arrayListOf()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        setOnClicks()
        setComments()
        checkForComments()
    }

    private fun checkForComments() {
        val uid = sharedPreferences.getString(UID, "uid")
        if (listComment.isNotEmpty()) {
            for (model in listComment) {
                if (uid == model.UID) {
                    rootView.maydonni_baholang.visibility = View.GONE
                    rootView.tv_fikr_bildiring.visibility = View.GONE
                    rootView.custom_ratingbar.visibility = View.GONE
                    listComment.remove(model)
                    Log.d("listComments","list: $listComment")
                    listComment.add(0, model)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                checkForComments()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callFirebaseForMaydonlar = CallFirebaseForMaydonlar()
        docPath = arguments?.getString("docPath")!!
        gson = Gson()
        sharedPreferences = activity?.getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )!!
        sharedPreferences.edit()
            .putString("docPath", docPath)
            .apply()

        val callback: ((ArrayList<ModelSharhlar>) -> Unit) = {
            listComment = it
            val json = gson.toJson(it)
            sharedPreferences.edit()
                .putString(docPath, json)
                .apply()
        }
        callFirebaseForMaydonlar.getComments(docPath, callback)
    }

    private fun setComments() {
        val json = sharedPreferences.getString(docPath, "")
        if (json != null && json.isNotEmpty()) {
            val type = object : TypeToken<ArrayList<ModelSharhlar>>() {}.type
            listComment = gson.fromJson(json, type)
            setUpRv(listComment)
        } else {
            val listComments: ((ArrayList<ModelSharhlar>) -> Unit) = {
                listComment = it
                setUpRv(it)
            }
            callFirebaseForMaydonlar.getComments(docPath, listComments)
        }

    }

    private fun setUpRv(list: ArrayList<ModelSharhlar>) {
        if (list.isEmpty()) {
            rootView.tv_sharh_topilmadi.visibility = View.VISIBLE
        } else {
            rootView.tv_sharh_topilmadi.visibility = View.GONE
            rootView.rv_maydon_comments.adapter = AdapterComments(requireContext(), listComment)
            rootView.rv_maydon_comments.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                )
            )
        }
    }

    private fun setOnClicks() {
        rootView.star1.setOnClickListener(this)
        rootView.star2.setOnClickListener(this)
        rootView.star3.setOnClickListener(this)
        rootView.star4.setOnClickListener(this)
        rootView.star5.setOnClickListener(this)
        rootView.nsv_maydon.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                rootView.fab.hide()
            } else {
                rootView.fab.show()

            }
        })
    }

    override fun invoke(response: ModelMaydon) {
        setImages(response.imagesPath)
        setGrades(response.bahosi, response.countOfComment)
        setViews(response.countOfViews)
        setOthers(response)
    }

    private fun setOthers(response: ModelMaydon) {
        rootView.tv_maydon_nomi.text = response.nomi
        rootView.tv_joylashuvi.setText("Manzil: ${response.address}")
        rootView.tv_moljal.text = "Mo'ljal: ${response.moljal}"
        if (response.tel.size == 2) {
            rootView.tv_tel1.text = "Tel: ${response.tel[0]}"
            rootView.tv_tel2.text = "Tel: ${response.tel[1]}"
        } else {
            rootView.tv_tel1.text = "Tel: ${response.tel[0]}"
            rootView.tv_tel2.visibility = View.GONE
        }

    }

    private fun setViews(countOfViews: Int) {
        rootView.tv_korilganlar_soni.text = countOfViews.toString()
    }

    private fun setGrades(bahosi: Float, countOfComment: Int) {
        rootView.tv_bahosi.text = bahosi.toString()
        rootView.tv_sharhlar_soni.text = "$countOfComment sharh"
    }

    private fun setImages(imagesPath: ArrayList<String>) {
        val callFirebaseStorage = CallFirebaseStorage()
        callFirebaseStorage.downloadNewsImages(imagesPath[0], rootView.iv_first_maydon, context!!)
        rootView.rv_images_maydonlar.adapter = ImageAdapter(context!!, imagesPath)
    }

    override fun onClick(v: View?) {
        setStarsClass().setStars(v!!, rootView, findNavController())
    }
}

class setStarsClass {
    fun setStars(
        v: View,
        rootView: View,
        navController: NavController?
    ): Int? {
        val star1 = rootView.star1
        val star2 = rootView.star2
        val star3 = rootView.star3
        val star4 = rootView.star4
        val star5 = rootView.star5
        val bundle = Bundle()
        when (v) {
            star1 -> {
                star1.setImageResource(R.drawable.ic_star_filled)
                star2.setImageResource(R.drawable.ic_star_empty)
                star3.setImageResource(R.drawable.ic_star_empty)
                star4.setImageResource(R.drawable.ic_star_empty)
                star5.setImageResource(R.drawable.ic_star_empty)
                if (navController != null) {
                    bundle.putInt("bahosi", 1)
                    navController.navigate(R.id.fragmentSharh, bundle)
                } else
                    return 1
            }
            star2 -> {
                star1.setImageResource(R.drawable.ic_star_filled)
                star2.setImageResource(R.drawable.ic_star_filled)
                star3.setImageResource(R.drawable.ic_star_empty)
                star4.setImageResource(R.drawable.ic_star_empty)
                star5.setImageResource(R.drawable.ic_star_empty)
                if (navController != null) {
                    bundle.putInt("bahosi", 2)
                    navController.navigate(R.id.fragmentSharh, bundleOf("bahosi" to 2))
                } else
                    return 2
            }
            star3 -> {
                star1.setImageResource(R.drawable.ic_star_filled)
                star2.setImageResource(R.drawable.ic_star_filled)
                star3.setImageResource(R.drawable.ic_star_filled)
                star4.setImageResource(R.drawable.ic_star_empty)
                star5.setImageResource(R.drawable.ic_star_empty)
                if (navController != null) {
                    bundle.putInt("bahosi", 3)
                    navController.navigate(R.id.fragmentSharh, bundleOf("bahosi" to 3))
                } else {
                    return 3
                }
            }
            star4 -> {
                star1.setImageResource(R.drawable.ic_star_filled)
                star2.setImageResource(R.drawable.ic_star_filled)
                star3.setImageResource(R.drawable.ic_star_filled)
                star4.setImageResource(R.drawable.ic_star_filled)
                star5.setImageResource(R.drawable.ic_star_empty)
                if (navController != null) {
                    bundle.putInt("bahosi", 4)
                    navController.navigate(R.id.fragmentSharh, bundleOf("bahosi" to 4))
                } else
                    return 4
            }
            star5 -> {
                star1.setImageResource(R.drawable.ic_star_filled)
                star2.setImageResource(R.drawable.ic_star_filled)
                star3.setImageResource(R.drawable.ic_star_filled)
                star4.setImageResource(R.drawable.ic_star_filled)
                star5.setImageResource(R.drawable.ic_star_filled)
                if (navController != null) {
                    bundle.putInt("bahosi", 5)
                    navController.navigate(R.id.fragmentSharh, bundleOf("bahosi" to 5))
                } else
                    return 5
            }
        }
        return null
    }
}