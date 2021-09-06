package com.openclassrooms.realestatemanager.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository


class EstateViewModel @ViewModelInject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------
    val estates = estateDataSource.getEstates()
    }
