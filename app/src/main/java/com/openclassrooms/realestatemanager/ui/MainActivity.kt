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
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.view.MenuItem
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private lateinit var detailFragment: DetailFragment
    private lateinit var masterFragment: MasterFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        onClickFab();
        configureAndShowMasterFragment()
        configureAndShowDetailFragment()
        setSupportActionBar(toolbar)
    }

    private fun configureAndShowMasterFragment() {
        //A - We only add DetailFragment in Tablet mode (If found frame_layout_detail)
        masterFragment = MasterFragment()
        supportFragmentManager.beginTransaction().add(R.id.frame_layout_main, masterFragment).commit()
    }

    private fun configureAndShowDetailFragment() {
        if (findViewById<View?>(R.id.frame_layout_detail) != null) {
            //Create new main fragment
            detailFragment = DetailFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_detail, detailFragment)
                .commit()
        }
    }

    /**
     * For click on fab for create new estate
     */
    fun onClickFab() {
        binding.fabBtn.setOnClickListener {
            val fabIntent = Intent(applicationContext, AddEditActivity::class.java)
            startActivity(fabIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    /**
     * For edit button and return button
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle actions on menu items
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.map_btn -> {
                val mapIntent = Intent(this,  MapActivity::class.java)
                startActivity(mapIntent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = binding.includedToolbar.simpleToolbar
    }

}