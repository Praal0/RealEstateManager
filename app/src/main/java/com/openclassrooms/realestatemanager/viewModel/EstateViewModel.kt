package com.openclassrooms.realestatemanager.viewModel

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.FullEstate
import com.openclassrooms.realestatemanager.model.Image
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.repositories.LocationDataRepository
import java.util.concurrent.Executor

class EstateViewModel {
    //REPOSITORY
    private var mExecutor: Executor? = null
    private lateinit var mEstateDataSource: EstateDataRepository


    // DATA
    private var mEstate: LiveData<List<FullEstate>>? = null

    fun EstateViewModel(estateDataSource: EstateDataRepository, executor: Executor
    ) {
        mEstateDataSource = estateDataSource
        mExecutor = executor
    }

    fun init() {
        if (mEstate == null) mEstate = mEstateDataSource.getEstates()
    }

    // --------------------
    // ESTATES
    // --------------------

    fun getEstates(estateId:Long): LiveData<FullEstate>{
        return mEstateDataSource.gesEstateByID(estateId)
    }








}