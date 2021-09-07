package com.openclassrooms.realestatemanager.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityDetailBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.AddEditActivity
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var activityDetailBinding: ActivityDetailBinding? = null
    private val detailFragment: DetailFragment? = null
    private val estate: Estate? = null
    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        val view: View = activityDetailBinding!!.root
        setContentView(view)

        this.configureToolbar()
        this.configureUpButton()
        this.configureAndShowDetailFragment()
        //for title toolbar
        val ab = supportActionBar
        Objects.requireNonNull(ab)?.setTitle("Estate Description")
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
        TODO("Not yet implemented")
    }

    private fun configureUpButton() {
        TODO("Not yet implemented")
    }

    private fun configureToolbar() {
        TODO("Not yet implemented")
    }
}