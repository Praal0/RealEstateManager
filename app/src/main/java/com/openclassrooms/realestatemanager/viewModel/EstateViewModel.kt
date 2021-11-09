package com.openclassrooms.realestatemanager.viewModel


import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.io.Files.map
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.ui.notification.Notification.sendNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    val currentPhoto: MutableLiveData<MutableList<Uri>> by lazy {
        MutableLiveData<MutableList<Uri>>()
    }

    val currentPhotoText: MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>()
    }

    // --------------------
    // ESTATES
    // --------------------

    fun insertEstates(estate: Estate,context: Context) {
        viewModelScope.launch {
            estateDataSource.createEstate(estate)
            sendNotification(context)
            delay(1000)
        }
    }

    fun updateEstate(estate: Estate){
        viewModelScope.launch {
            estateDataSource.updateEstate(estate)
            delay(10)
        }
    }

    fun selectItem(estate: Long) = estateDataSource.setCurrentEstateId(estate)


    fun getEstates() : LiveData<List<Estate>>{
        return estateDataSource.getEstates()
    }

    fun getEstateById(estateId: Long) : LiveData<Estate>{
        return estateDataSource.getEstateByID(estateId)
    }

    val currentEstate = estateDataSource.currentEstateIdFlow

}
