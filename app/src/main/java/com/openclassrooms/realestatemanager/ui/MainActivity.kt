package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.master.MasterFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private var detailFragment: DetailFragment? = null
    private var masterFragment: MasterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        onClickFab()
        configureAndShowMasterFragment()
        configureAndShowDetailFragment()

        setSupportActionBar(toolbar)
    }

    private fun configureAndShowMasterFragment() {
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        masterFragment = getSupportFragmentManager().findFragmentById(com.openclassrooms.realestatemanager.R.id.frame_layout_main) as MasterFragment?

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (masterFragment == null ) {
            masterFragment = MasterFragment()
            supportFragmentManager.beginTransaction()
                .add(com.openclassrooms.realestatemanager.R.id.frame_layout_main, masterFragment!!)
                .commit()
        }
    }

    private fun configureAndShowDetailFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = supportFragmentManager.findFragmentById(com.openclassrooms.realestatemanager.R.id.frame_layout_detail) as DetailFragment?

        if (detailFragment == null && findViewById<View?>(com.openclassrooms.realestatemanager.R.id.frame_layout_detail) != null) {
            //Create new main fragment
            detailFragment = DetailFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(com.openclassrooms.realestatemanager.R.id.frame_layout_detail, detailFragment!!)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.openclassrooms.realestatemanager.R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    fun onClickFab() {
        binding.fabBtn.setOnClickListener {  }
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = binding.includedToolbar.simpleToolbar
    }

}