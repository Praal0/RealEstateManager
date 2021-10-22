package com.openclassrooms.realestatemanager.ui.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.map.MapsActivity
import com.openclassrooms.realestatemanager.ui.search.SearchActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainAdapter.MainItemListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private lateinit var adapter: MainAdapter
    private lateinit var detailFragment : DetailFragment
    val estateViewModel: EstateViewModel by viewModels()
    val locationViewModel : LocationViewModel by viewModels()
    // Is the container layout available? If so, set mTwoPane to true.
    private var mTwoPane: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolbar = binding.includedToolbar.simpleToolbar
        setContentView(binding.root)
        onClickFab()
        setupRecyclerView()
        setupObservers()
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        adapter = MainAdapter(this)
        binding.fragmentListRV.layoutManager = LinearLayoutManager(this)
        binding.fragmentListRV.adapter = adapter

        // Is the container layout available? If so, set mTwoPane to true.
        if (findViewById<View>(R.id.frame_layout_detail) != null) {
            mTwoPane = true
            detailFragment = DetailFragment()
            //Add it to FrameLayout container
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout_detail, detailFragment)
                .commit()
        }
    }

    private fun setupObservers() {
        this.estateViewModel.getEstates().observe(this, this::updateEstateList)
    }


    /**
     * for update estate list
     * @param estates
     */
    private fun updateEstateList(estates: List<Estate>?) {
        if (estates != null) adapter.updateData(estates,locationViewModel, Glide.with(this),this)
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

    override fun onClickedEstate(estate: Estate) {
        //estateViewModel.setCurrentEstate(estate)
        if (!Utils.isTablet(this)){
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("estate", estate.numMandat)
            Log.d("bundleRV", "estate$estate")
            startActivity(intent)
    }


}
}