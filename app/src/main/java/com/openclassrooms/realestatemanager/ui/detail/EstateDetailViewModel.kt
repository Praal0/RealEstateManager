package com.openclassrooms.realestatemanager.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.utils.Resource

class EstateDetailViewModel @ViewModelInject constructor(
    private val repository: EstateDataRepository
) : ViewModel() {

    private val _id = MutableLiveData<Long>()

    private val _estate = _id.switchMap { id ->
        repository.gesEstateByID(id)
    }
    val estate: LiveData<Resource<Estate>> = _estate


    fun start(id: Long) {
        _id.value = id
    }


}
