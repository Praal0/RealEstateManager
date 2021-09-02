package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.master.MasterFragment
import android.content.Intent
import com.openclassrooms.realestatemanager.ui.add.AddActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        masterFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_main) as MasterFragment?

        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (masterFragment == null ) {
            masterFragment = MasterFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_main, masterFragment!!)
                .commit()
        }
    }

    private fun configureAndShowDetailFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_detail) as DetailFragment?

        if (detailFragment == null && findViewById<View?>(R.id.frame_layout_detail) != null) {
            //Create new main fragment
            detailFragment = DetailFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_detail, detailFragment!!)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    /**
     * For click on fab for create new estate
     */
    fun onClickFab() {
        binding.fabBtn.setOnClickListener {
            val fabIntent = Intent(applicationContext, AddActivity::class.java)
            startActivity(fabIntent)
        }
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = binding.includedToolbar.simpleToolbar
    }

}