package com.openclassrooms.realestatemanager.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor (private val estateDataSource: EstateDataRepository): ViewModel(){
    fun searchEstate(
        estateType: String?,
        city: String?,
        minRooms: Int?,
        maxRooms: Int?,
        minSurface: Int?,
        maxSurface: Int?,
        minPrice: Double?,
        maxPrice: Double?,
        minUpOfSaleDate: String?,
        maxOfSaleDate: String?,
        minSoldDate : String?,
        maxSoldDate : String?,
        photos: Boolean,
        schools: Boolean,
        stores: Boolean,
        park: Boolean,
        restaurants: Boolean,
        sold: Boolean
    ): LiveData<List<Estate>>  {
        var queryString = ""
        val args: MutableList<String> = ArrayList()
        var containsCondition = false

        queryString += "SELECT * FROM Estate "

        if (estateType?.isNotEmpty() == true) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " estateType=?"
            args.add(estateType)
        }

        if (city?.isNotEmpty() == true) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " city=?"
            args.add(city)
        }

        if (minRooms != null && maxRooms != null && minRooms >= 0 && maxRooms > 0) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE";
                containsCondition = true
            }
            queryString += "rooms BETWEEN ? AND ?"
            args.add(minRooms.toString())
            args.add(maxRooms.toString())
        }
        if (minSurface != null && maxSurface != null && minSurface >= 0 && maxSurface > 0) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " surface BETWEEN ? AND ?"
            args.add(minSurface.toString())
            args.add(maxSurface.toString())
        }
        if (minPrice != null && maxPrice != null && minPrice >= 0 && maxPrice > 0) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " price BETWEEN ? AND ?"
            args.add(minPrice.toString())
            args.add(maxPrice.toString())
        }
        if (minUpOfSaleDate != null && maxOfSaleDate != null ) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " upOfSaleDate BETWEEN ? AND ?";
            args.add(minUpOfSaleDate.toString())
            args.add(maxOfSaleDate.toString())
        }
        if (minSoldDate != null && maxSoldDate != null) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " soldDate BETWEEN ? AND ?";
            args.add(minUpOfSaleDate.toString())
            args.add(maxOfSaleDate.toString())
        }
        if (photos) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " photoList <> ''"
        }
        if (schools) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " schools = 1"
        }
        if (stores) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " stores = 1";
        }

        if (park) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE"
                containsCondition = true;
            }
            queryString += " park = 1";
        }

        if (restaurants) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " restaurants = 1"
            containsCondition = true
        }

        if (sold.equals(true)) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " sold = 1"
        }

        Log.d("queryString", "queryString" + queryString);

        return estateDataSource.getSearchEstate(queryString, args);
    }


}