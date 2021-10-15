package com.openclassrooms.realestatemanager.viewModel


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.ui.notification.Notification.sendNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

      var currentEstate : MutableLiveData<LiveData<Estate>>? = null

    fun setCurrentEstate(estateId: Long) {
        currentEstate?.postValue(estateDataSource.getEstateByID(estateId))
        Log.d("Estate Data Source: ", estateDataSource.toString())
    }

    fun getEstates() : LiveData<List<Estate>>{
        return estateDataSource.getEstates()
    }

    fun getEstateById(estateId: Long) : LiveData<Estate>{
        return estateDataSource.getEstateByID(estateId)
    }
}
