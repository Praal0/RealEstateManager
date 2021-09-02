package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository


class EstateViewModel (
    private val estateDataSource: EstateDataRepository
    ): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------
    val estates = estateDataSource.getEstates()
    }
