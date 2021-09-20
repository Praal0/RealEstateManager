package com.openclassrooms.realestatemanager.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private lateinit var detailFragment: DetailFragment
    private lateinit var toolbar : Toolbar
    private var estate: Estate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        val view: View = activityDetailBinding.root
        setContentView(view)
        initialize()
        setSupportActionBar(toolbar)
        this.configureAndShowDetailFragment()
    }

    /**
     * For edit button
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle actions on menu items
        return when (item.itemId) {
            R.id.edit_btn -> {
                val idEstate: Long? = estate?.id
                val editIntent = Intent(this, AddEditActivity::class.java)
                editIntent.putExtra("iDEstate", idEstate)
                Log.d("editEstate", "editEstate$idEstate")
                startActivity(editIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureAndShowDetailFragment() {
        //Create new main fragment
        detailFragment = DetailFragment()
        //Add it to FrameLayout container
        supportFragmentManager.beginTransaction().add(R.id.detail_fragment_frameLayout, detailFragment).commit()

    }

    override fun onResume() {
        super.onResume()
        // Call update method here because we are sure that DetailFragment is visible
        updateDetailUiForFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.description_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    // Initialisation variable
    private fun initialize() {
        toolbar = activityDetailBinding.includedToolbarDetail.simpleToolbar
    }

    /**
     * For data for tablet
     */
    private fun updateDetailUiForFragment() {
        val intentTablet = Objects.requireNonNull(this).intent
        estate = intentTablet.getSerializableExtra("estateId") as Estate?
        Log.d("estateDetail", "estateDetail$estate")
    }


}