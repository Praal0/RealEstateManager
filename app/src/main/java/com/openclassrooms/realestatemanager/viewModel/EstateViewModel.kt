package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.FullEstate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class EstateViewModel  @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    // --------------------
    // ESTATES
    // --------------------
    fun getEstates() = estateDataSource.getEstates()

    fun getEstateByID(estateId:Long): LiveData<FullEstate> = estateDataSource.gesEstateByID(estateId)









}