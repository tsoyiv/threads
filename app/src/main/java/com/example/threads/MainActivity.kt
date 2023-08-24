package com.example.threads

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    val CHANEL_ID = "chanelID"
    val CHANEL_NAME = "chanelName"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.btn_nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.btn_nav_view)
            .setupWithNavController(navController)

        createNotification()
    }

    fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ID, CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    //    override fun onResume() {
//        super.onResume()
////        bottomNavigationView.menu.findItem(R.id.menu_item_home).setIcon(R.drawable.img_home_filled)
//        bottomNavigationView.menu.findItem(R.id.tabMainFeedFragment).setIcon(R.drawable.img_colored_home)
//    }
//    override fun onStop() {
//        super.onStop()
//        bottomNavigationView.menu.findItem(R.id.tabMainFeedFragment).setIcon(R.drawable.icon_home)
//      //bottomNavigationView.menu.findItem(R.id.menu_item_search).setIcon(R.drawable.img_search_unfocused)
//    }
    fun hide() {
        val btmNavBar = findViewById<View>(R.id.btn_nav_view)
        btmNavBar.visibility = View.GONE
    }

    fun showBtm() {
        val btmNavBar = findViewById<View>(R.id.btn_nav_view)
        btmNavBar.visibility = View.VISIBLE
    }
}