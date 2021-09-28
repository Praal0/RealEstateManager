package com.openclassrooms.realestatemanager.repositories

import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.models.Location


class LocationDataRepository(private val locationDao: LocationDao) {



}