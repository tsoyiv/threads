package com.example.threads

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.threads.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.btn_nav_view)
            .setupWithNavController(navController)
    }

    fun hide() {
        val btmNavBar = findViewById<View>(R.id.btm_nav_bar)
        btmNavBar.visibility = View.GONE
    }

    fun showBtm() {
        val btmNavBar = findViewById<View>(R.id.btm_nav_bar)
        btmNavBar.visibility = View.VISIBLE
    }
}