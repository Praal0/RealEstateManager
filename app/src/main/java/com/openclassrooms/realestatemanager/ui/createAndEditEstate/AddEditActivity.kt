package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding


class AddEditActivity : AppCompatActivity() {

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater);
        estateFormBinding = activityAddBinding.includeForm;

        val view: View = activityAddBinding.root
        setContentView(view)
    }
}