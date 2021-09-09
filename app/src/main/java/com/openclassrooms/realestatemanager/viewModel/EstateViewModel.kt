package com.openclassrooms.realestatemanager.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------


    fun insertEstates(estate: Estate) {
        estateDataSource.createEstate(estate)
    }

    fun getEstates() : LiveData<List<Estate>>{
        return estateDataSource.getEstates()
    }
}
