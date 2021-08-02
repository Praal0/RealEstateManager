package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.openclassrooms.realestatemanager.R

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var drawer : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        initNavigationdrawer()
        setSupportActionBar(toolbar)
    }

    // Initialisation drawer
    private fun initNavigationdrawer() {
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = findViewById(R.id.simple_toolbar)
        drawer = findViewById(R.id.drawer_layout)
    }

}