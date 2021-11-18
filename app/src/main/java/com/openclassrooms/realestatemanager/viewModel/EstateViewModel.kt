package com.openclassrooms.realestatemanager.viewModel


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.ui.notification.Notification.sendNotification
import com.openclassrooms.realestatemanager.utils.EstateManagerStream
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel() {

    val currentEstateId = estateDataSource.currentEstateIdFlow

    private var listPhoto : MutableList<Uri> = ArrayList()
    private var listDescription : MutableList<String> = ArrayList()

    var currentPhoto = MutableLiveData<MutableList<Uri>>()
    init{
        listPhoto.clear()
        currentPhoto.value = ArrayList()
    }

    var currentPhotoText = MutableLiveData<MutableList<String>>()
    init{
        currentPhotoText.value = ArrayList()
        listDescription.clear()
    }

    val  currentEstate : MutableLiveData<Estate> by lazy {
        MutableLiveData<Estate>()
    }

    // --------------------
    // ESTATES
    // --------------------


    fun setCurrentPhoto(uri : Uri) {
        listPhoto.add(uri)
        currentPhoto.value = listPhoto
    }

    fun setCurrentPhotoDescription(description : String) {
        listDescription.add(description)
        currentPhotoText.value = listDescription
    }

    fun setCurrentEstate(estate: Estate){
        currentEstate.value = estate
    }

    fun insertEstates(estate: Estate,context: Context) {
        viewModelScope.launch {
            val result = estateDataSource.createEstate(estate)
            if (result){
                sendNotification(context,context.getString(R.string.the_new_property_has_been_successfully_created))
            }else{
                sendNotification(context,context.getString(R.string.the_new_property_has_not_created))
            }

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



}
