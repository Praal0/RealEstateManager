package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class EstateViewModel  @Inject constructor (
    private val estateDataSource: EstateDataRepository
    ): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------
    fun getEstates() = estateDataSource.getEstates()

    val estate = estateDataSource.getEstates()
    }
