package com.openclassrooms.realestatemanager.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------


    fun insertEstates(estate: Estate) {
        viewModelScope.launch {
            estateDataSource.createEstate(estate)
            delay(1_000)
        }

    }

    fun getEstates() : LiveData<List<Estate>>{
        return estateDataSource.getEstates()
    }
}
