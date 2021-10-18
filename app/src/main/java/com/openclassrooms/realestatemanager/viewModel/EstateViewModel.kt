package com.openclassrooms.realestatemanager.viewModel


import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.ui.notification.Notification.sendNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.acl.Owner
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

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

    fun deleteEstate(estateId : Long){
        viewModelScope.launch {
            estateDataSource.deleteEstate(estateId)
            delay(1000)
        }

    }

    val currentEstate = MutableLiveData<Estate>()

    fun setCurrentEstate(estateId: Long) {
        currentEstate.value = estateDataSource.getEstateByID(estateId).value
    }

    fun getEstates() : LiveData<List<Estate>>{
        return estateDataSource.getEstates()
    }

    fun getEstateById(estateId: Long) : LiveData<Estate>{
        return estateDataSource.getEstateByID(estateId)
    }
}
