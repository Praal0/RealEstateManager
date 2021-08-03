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
    private lateinit var mImageDataSource: ImageDataRepository
    private lateinit var mEstateDataSource: EstateDataRepository
    private lateinit var mLocationDataSource :LocationDataRepository

    // DATA
    private var mEstate: LiveData<List<FullEstate>>? = null

    fun TaskViewModel(estateDataSource: EstateDataRepository, imageDataSource: ImageDataRepository,locationDataSource: LocationDataRepository, executor: Executor
    ) {
        mEstateDataSource = estateDataSource
        mImageDataSource = imageDataSource
        mLocationDataSource = locationDataSource
        mExecutor = executor
    }

    fun init() {
        if (mEstate == null) mEstate = mEstateDataSource.getEstates()
    }

    // --------------------
    // IMAGE
    // --------------------

    fun getImages(estateId:Long): LiveData<List<Image>>{
        return mImageDataSource.getImages(estateId)
    }








}