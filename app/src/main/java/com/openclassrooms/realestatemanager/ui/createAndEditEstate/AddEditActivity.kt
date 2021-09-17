package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.databinding.EstateFormBinding
import java.util.*
import com.openclassrooms.realestatemanager.models.Estate

class AddEditActivity : AppCompatActivity() {

    private lateinit var activityAddBinding: ActivityAddEditBinding
    private lateinit var estateFormBinding: EstateFormBinding
    private lateinit var toolbar : Toolbar
    private var  estateEdit : Long = 0
    private  var estate:Estate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAddBinding = ActivityAddEditBinding.inflate(layoutInflater)
        estateFormBinding = activityAddBinding.includeForm;

        val view: View = activityAddBinding.root
        setContentView(view)


        estateFormBinding.videoView.visibility = View.INVISIBLE

        if (estateEdit == 0L) {
            Objects.requireNonNull(estateFormBinding.deleteVideo).setVisibility(View.INVISIBLE)
        }
    }
}