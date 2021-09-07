package com.openclassrooms.realestatemanager.viewModel


import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------
    val estates = estateDataSource.getEstates()
    }
