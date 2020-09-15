package com.sample.firebaseexample.ui.main_screen.ui.Fragment_sharh

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.sharhlar.CallFirebaseForSharhlar
import com.sample.firebaseexample.ui.main_screen.UID
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.setStarsClass
import kotlinx.android.synthetic.main.fragment_sharh.view.*

class FragmentSharh : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var callFirebaseForSharhlar: CallFirebaseForSharhlar
    private var bahosi: Int? = null
    private lateinit var sharh: String
    private lateinit var docPath: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sharh, container, false)
        Log.e("bahosi",arguments?.getInt("bahosi").toString())
        docPath = activity!!.getSharedPreferences(resources.getString(R.string.shared_preference_key),Context.MODE_PRIVATE)
            .getString("docPath","")!!
        Log.e("docPath","docPath: $docPath")
        Log.e("docPathSharh","docPath $docPath")
        if (arguments?.getInt("bahosi") != null) {
            bahosi = arguments?.getInt("bahosi")!!
            setGrade(bahosi!!)
        }

        callFirebaseForSharhlar = CallFirebaseForSharhlar()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        rootView.et_sharh.requestFocus()
       val imm =  activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(rootView.et_sharh,InputMethodManager.SHOW_IMPLICIT)
        rootView.star1.setOnClickListener(this)
        rootView.star2.setOnClickListener(this)
        rootView.star3.setOnClickListener(this)
        rootView.star4.setOnClickListener(this)
        rootView.star5.setOnClickListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_comment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setGrade(bahosi: Int) {
        when (bahosi) {
            1 -> {
                rootView.star1.setImageResource(R.drawable.ic_star_filled)
            }
            2 -> {
                rootView.star1.setImageResource(R.drawable.ic_star_filled)
                rootView.star2.setImageResource(R.drawable.ic_star_filled)
            }
            3 -> {
                rootView.star1.setImageResource(R.drawable.ic_star_filled)
                rootView.star2.setImageResource(R.drawable.ic_star_filled)
                rootView.star3.setImageResource(R.drawable.ic_star_filled)
            }
            4 -> {
                rootView.star1.setImageResource(R.drawable.ic_star_filled)
                rootView.star2.setImageResource(R.drawable.ic_star_filled)
                rootView.star3.setImageResource(R.drawable.ic_star_filled)
                rootView.star4.setImageResource(R.drawable.ic_star_filled)
            }
            5 -> {
                rootView.star1.setImageResource(R.drawable.ic_star_filled)
                rootView.star2.setImageResource(R.drawable.ic_star_filled)
                rootView.star3.setImageResource(R.drawable.ic_star_filled)
                rootView.star4.setImageResource(R.drawable.ic_star_filled)
                rootView.star5.setImageResource(R.drawable.ic_star_filled)
            }
        }
    }

    override fun onClick(v: View?) {
        bahosi = setStarsClass().setStars(v!!, rootView, null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("onOptionsItemSelected", "menu item pressed")
        sharh = rootView.et_sharh.text.toString()
        val sharedPreferences =
            activity!!.getSharedPreferences(resources.getString(R.string.shared_preference_key), Context.MODE_PRIVATE)
        docPath = sharedPreferences.getString("docPath", "")!!
        Log.d("docPathSharh", docPath)
        if (docPath != "") {
            val uid = sharedPreferences.getString(UID,"uid")
            callFirebaseForSharhlar.saveSharhlar(bahosi!!, sharh, docPath,uid!!)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        showDialog()
    }

    private fun showDialog() {
        val uid = activity?.getSharedPreferences("UID", Context.MODE_PRIVATE)
            ?.getString("UID", "uid")
        Log.e("UID","uid: $uid")
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Qoralama bekor qilinsinmi?")
            .setPositiveButton("Bekor qilish",
                DialogInterface.OnClickListener { dialog, which ->
                return@OnClickListener})
            .setNegativeButton("Saqlash",
                DialogInterface.OnClickListener { dialog, which ->
                    sharh = rootView.et_sharh.text.toString()
                    callFirebaseForSharhlar.saveSharhlar(bahosi!!, sharh, docPath,uid!!)
                    return@OnClickListener
                })
            alertDialog.show()
    }
}