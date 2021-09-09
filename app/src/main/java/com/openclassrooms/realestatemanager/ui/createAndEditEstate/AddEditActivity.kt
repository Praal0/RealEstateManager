package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import java.util.*

class AddEditActivity : AppCompatActivity() {

    private var activityAddBinding: ActivityAddEditBinding? = null
    private var estateFormBinding: EstateFormBinding? = null
    private var estateEdit: Long = 0
    private val idEstate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for view binding
        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding!!.includeForm

        val view: View = activityAddBinding!!.getRoot()
        setContentView(view)

        estateEdit = intent.getLongExtra("iDEstate", idEstate)


        onClickPhotoBtn()
        //for title toolbar
        val ab: ActionBar? = supportActionBar
        ab?.let {
            if (estateEdit == 0L) {
                it.setTitle("Create Estate")
            } else {
                it.setTitle("Edit Estate")
            }
        }

    }

    /**
     * For click on photo btn
     */
    fun onClickPhotoBtn() {
        estateFormBinding?.photoBtn?.setOnClickListener { v ->
            selectImage()
            saveImageInInternalStorage()
        }
    }

    private fun saveImageInInternalStorage() {
        TODO("Not yet implemented")
    }

    private fun selectImage() {
        TODO("Not yet implemented")
    }

    /**
     * For click on video btn
     */
    fun onClickVideoBtn() {

    }
}