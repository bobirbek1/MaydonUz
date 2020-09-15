package com.sample.firebaseexample.ui.main_screen

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.sample.firebaseexample.R
import com.sample.firebaseexample.model.maydonlar.CallFirebaseForMaydonlar
import com.sample.firebaseexample.model.models.ModelNews
import com.sample.firebaseexample.model.news.CallFirebaseForNews
import com.sample.firebaseexample.ui.main_screen.ui.fragmentMaydon.LIST_MAYDONLAR
import com.sample.firebaseexample.ui.main_screen.ui.home.CallbackNews
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.content_main_screen.*

const val UID = "UID"
const val LIST_NEWS = "list_news"

class MainScreenActivity : AppCompatActivity(), CallbackNews {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        this.getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(UID, firebaseAuth.currentUser?.uid)
            .apply()
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.setNavigationOnClickListener {
//            drawer_layout.openDrawer(nav_view)
//        }

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bot_nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send,
                R.id.bot_nav_map, R.id.bot_nav_favourite
            )
        )
        nav_view.setupWithNavController(navController)
        bottom_navigation.setupWithNavController(navController)

    }

    override fun onResume() {
        super.onResume()
        saveData()
    }

    private fun saveData() {
        CallFirebaseForNews().getNews(this)
        CallFirebaseForMaydonlar().getMaydonlar {
            this.getSharedPreferences(
                resources.getString(R.string.shared_preference_key),
                Context.MODE_PRIVATE
            )
                .edit()
                .putString(LIST_MAYDONLAR, Gson().toJson(it))
                .apply()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_screen, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    fun onHeaderClick() {
        drawer_layout.closeDrawer(GravityCompat.START)
        bottom_navigation.visibility = BottomNavigationView.GONE
        this.navController
            .navigate(R.id.fragment_profil)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        bottom_navigation.visibility = BottomNavigationView.VISIBLE
    }

    override fun getNews(response: ArrayList<ModelNews>) {
        val json = Gson().toJson(response)
        this.getSharedPreferences(
            resources.getString(R.string.shared_preference_key),
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(LIST_NEWS, json)
            .apply()
    }

    override fun updateNews(response: Boolean) {

    }


}
