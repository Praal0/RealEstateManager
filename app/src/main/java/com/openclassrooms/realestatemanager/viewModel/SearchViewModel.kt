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
        minUpOfSaleDate: Long?,
        maxOfSaleDate: Long?,
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
                containsCondition = true;
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
            }
            queryString += " price BETWEEN ? AND ?"
            args.add(minPrice.toString())
            args.add(maxPrice.toString())
        }
        if (minUpOfSaleDate != null && maxOfSaleDate != null && minUpOfSaleDate >= 0 && maxOfSaleDate > 0) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
            }
            queryString += " upOfSaleDate BETWEEN ? AND ?";
            args.add(minUpOfSaleDate.toString())
            args.add(maxOfSaleDate.toString())
        }
        if (photos) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
            }
            queryString += " photoList <> ''"
        }
        if (schools) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
            }
            queryString += " schools = 1"
        }
        if (stores) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
            }
            queryString += " stores = 1";
        }

        if (park) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
            }
            queryString += " park = 1";
        }

        if (restaurants == true) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
            }
            queryString += " restaurants = 1";
        }

        if (sold.equals(true)) {
            if (containsCondition) {
                queryString += " AND";
            } else {
                queryString += " WHERE";
            }
            queryString += " sold = 1";
        }

        Log.d("queryString", "queryString" + queryString);

        return estateDataSource.getSearchEstate(queryString, args);
    }


}