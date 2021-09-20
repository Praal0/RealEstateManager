package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.app.ActionBar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import java.util.*
import android.app.DatePickerDialog
import android.widget.MediaController
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat


class AddEditActivity : AppCompatActivity() {

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar

    private var mUpOfSaleDateDialog: DatePickerDialog? = null
    private var mDateFormat: SimpleDateFormat? = null
    private var mSoldDate: DatePickerDialog? = null
    private var estateEdit: Long = 0
    private val idEstate: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm
        estateEdit = intent.getLongExtra("iDEstate", idEstate)


        val view: View = activityAddBinding.root
        setContentView(view)
        initialize()
        dropDownAdapters()

        setSupportActionBar(toolbar)

        //for title toolbar
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        if (estateEdit == 0L) {
            ab?.setTitle("Create Estate")
        } else {
            ab?.setTitle("Edit Estate")
        }

        //For date picker
        mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        //For hide mandate number
        estateFormBinding.etMandate.visibility = View.INVISIBLE
        estateFormBinding.inputMandate.visibility = View.INVISIBLE


        //for video in edit
        estateFormBinding.videoView.requestFocus()
        val mediaController = MediaController(this)
        estateFormBinding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(estateFormBinding.videoView)
        estateFormBinding.videoView.start()

    }


    /**
     * Adapter generic for dropdown
     * @param resId
     * @return
     */
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?>? {
        return ArrayAdapter(
            this, R.layout.dropdown_menu_popup_item,
            resources.getStringArray(resId)
        )
    }

    // Initialisation variable
    private fun initialize() {
        toolbar = estateFormBinding.includedToolbarAdd.simpleToolbar
    }


    /**
     * For set adapters dropdown
     */
    fun dropDownAdapters() {
        estateFormBinding.etRooms.setAdapter(factoryAdapter(R.array.ROOMS))
        estateFormBinding.etBedrooms.setAdapter(factoryAdapter(R.array.BEDROOMS))
        estateFormBinding.etBathrooms.setAdapter(factoryAdapter(R.array.BATHROOMS))
        estateFormBinding.etAgent.setAdapter(factoryAdapter(R.array.AGENT))
    }
}