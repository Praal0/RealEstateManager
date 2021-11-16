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
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.map.MapsActivity
import com.openclassrooms.realestatemanager.ui.master.MasterFragment
import com.openclassrooms.realestatemanager.ui.search.SearchActivity
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private var masterFragment: MasterFragment? = null
    private var detailFragment: DetailFragment? = null


    private var idEstate: Long = 0

    val estateViewModel: EstateViewModel by viewModels()

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
        //Handle actions on menu items
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.edit_btn ->{
                idEstate = estateViewModel.currentEstate
                if (idEstate > 0) {
                    val editIntent = Intent(this, AddEditActivity::class.java)
                    editIntent.putExtra("iDEstate", idEstate)
                    startActivity(editIntent)
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

        if (masterFragment == null){
            masterFragment = MasterFragment.newInstance()
        }
        masterFragment?.let {supportFragmentManager.beginTransaction().replace(R.id.list_master_frameLayout, it).commit()}
    }

    private fun configureAndShowDetailFragment(){
        //Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        if (findViewById<View?>(R.id.detail_fragment_frameLayout) != null) {
            //Create new main fragment
            if (detailFragment == null){
                detailFragment = DetailFragment.newInstance()
            }
            //Add it to FrameLayout container
            detailFragment?.let { supportFragmentManager.beginTransaction().replace(R.id.detail_fragment_frameLayout, it).commit() }
        }
    }
}
