package com.openclassrooms.realestatemanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.map.MapsActivity
import com.openclassrooms.realestatemanager.ui.master.MasterFragment
import com.openclassrooms.realestatemanager.ui.search.SearchActivity
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private var masterFragment: MasterFragment = MasterFragment()
    private var detailFragment: DetailFragment = DetailFragment()

    private var idEstate: Long = 0

    val estateViewModel: EstateViewModel by viewModels()
    val locationViewModel : LocationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = binding.includedToolbar.simpleToolbar
        setContentView(binding.root)
        onClickFab()
        setSupportActionBar(toolbar)
        methodRequiresTwoPermission()
        configureAndShowListFragment()
        configureAndShowDetailFragment()
    }

    /**
     * For edit button and return button
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val intent: Intent = intent
        val estateDetail = intent.getSerializableExtra("estate") as Estate?
        idEstate = estateDetail?.numMandat ?: 0
        //Handle actions on menu items
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.edit_btn ->{
                if (idEstate > 0) {
                    val editIntent = Intent(this, AddEditActivity::class.java)
                    editIntent.putExtra("estate", idEstate)
                    startActivity(editIntent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "No estate selected", Snackbar.LENGTH_SHORT).show()
                }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    /**
     * For click on fab for create new estate
     */
    private fun onClickFab() {
        binding.fabBtn.setOnClickListener {
            val fabIntent = Intent(applicationContext, AddEditActivity::class.java)
            startActivity(fabIntent)
        }
    }

    /**
     * For connecting fragment list
     */
    private fun configureAndShowListFragment() {
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        masterFragment = MasterFragment()
        supportFragmentManager.beginTransaction().add(R.id.list_master_frameLayout, masterFragment).commit()
    }

    private fun configureAndShowDetailFragment(){
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        if (findViewById<View?>(R.id.detail_fragment_frameLayout) != null) {
            //Create new main fragment
            detailFragment = DetailFragment()

            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.detail_fragment_frameLayout, detailFragment)
                .commit()
        }
    }
}
