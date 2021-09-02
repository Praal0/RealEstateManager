package com.openclassrooms.realestatemanager.ui.add

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.Picture
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import java.util.concurrent.Executor

class EstateCreateViewModel (private val mEstateDataRepository: EstateDataRepository, private val mExecutor: Executor): ViewModel(){
    // --- CREATE ---
    fun createProperty(estate: Estate, pictureList: ArrayList<Picture>, context: Context) {
        try {
            mExecutor.execute {mEstateDataRepository.createEstate(estate, pictureList)}
        }catch ( e:Exception){
            Toast.makeText(context, context.getString(R.string.error_when_creating_the_property), Toast.LENGTH_LONG).show()
        }
    }
}