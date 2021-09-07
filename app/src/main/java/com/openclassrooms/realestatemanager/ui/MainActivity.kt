package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.master.MasterFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar : Toolbar
    private var detailFragment: DetailFragment? = null
    private var masterFragment: MasterFragment? = null
    private var idEstate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
        configureAndShowMasterFragment()
        configureAndShowDetailFragment()
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent = Objects.requireNonNull(intent)
        val estateDetail = intent.getSerializableExtra("estate") as Estate?
        idEstate = if (estateDetail != null) estateDetail.id else 0
        return when (item.getItemId()) {
            R.id.edit_btn -> {
                if (idEstate > 0) {
                    val editIntent = Intent(this, AddEditActivity::class.java)
                    editIntent.putExtra("iDEstate", idEstate)
                    startActivity(editIntent)
                    finish()
                } else {
                    Snackbar.make(binding.root, "No estate selected", Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureAndShowMasterFragment() {
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        masterFragment = supportFragmentManager.findFragmentById(com.openclassrooms.realestatemanager.R.id.frame_layout_main) as MasterFragment?

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

    // Initialisation variable
    private fun initialize() {
        toolbar = binding.includedToolbar.simpleToolbar
    }

}