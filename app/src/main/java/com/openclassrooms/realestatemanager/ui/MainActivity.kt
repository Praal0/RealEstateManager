package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.master.MasterFragment
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.view.MenuItem
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.ui.map.MapsActivity
import com.openclassrooms.realestatemanager.ui.search.SearchActivity










@AndroidEntryPoint
abstract class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private  var detailFragment: DetailFragment? = null
    private  var masterFragment: MasterFragment? = null

    private val perms = "Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION"



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
        masterFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_main) as MasterFragment
        if (masterFragment == null) {
            //Create new main fragment
            masterFragment = MasterFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_main, masterFragment!!)
                .commit()
        }


        supportFragmentManager.beginTransaction().add(R.id.frame_layout_main, masterFragment!!).commit()
    }

    private fun configureAndShowDetailFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = supportFragmentManager.findFragmentById(R.id.frame_layout_detail) as DetailFragment

        if (findViewById<View>(R.id.frame_layout_detail) != null ) {
            //Create new main fragment
            detailFragment = DetailFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_detail, detailFragment!!)
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

            R.id.search_btn -> {
                val searchIntent = Intent(this,SearchActivity::class.java)
                startActivity(searchIntent)
                return true
            }

            R.id.map_btn -> {
                val mapIntent = Intent(this,  MapsActivity::class.java)
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