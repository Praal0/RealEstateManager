package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.models.Estate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class EstateDataRepository @Inject constructor(private val estateDAO: EstateDAO) {

    var currentEstateIdFlow : Long = 0

    fun setCurrentEstateId(estateId: Long) {
        currentEstateIdFlow = estateId
    }

    fun getEstates(): LiveData<List<Estate>> {
        return this.estateDAO.getEstates()
    }

    fun getEstateByID(estateID:Long) : LiveData<Estate>{
        return this.estateDAO.getEstate(estateID)
    }

    /**
     * Create
     * @param estate
     */
    // --- CREATE ---
    suspend fun createEstate(estate: Estate) : Boolean {
        try {
            estateDAO.insertEstate(estate)
            return true
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("EstateDataRepository","Cannot Insert : $cause")
            return false
        }

    }


    /**
     * Update
     * @param estate
     */
    // --- UPDATE ---
    suspend fun updateEstate(estate: Estate) {
        try {
            estateDAO.updateEstate(estate)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("EstateDataRepository","Cannot Update : $cause")
        }
    }

    /**
     * For Search
     * @param queryString
     * @param args
     * @return
     */
    fun getSearchEstate(queryString: String?, args: List<Any?>): LiveData<List<Estate>> {
        val query: SupportSQLiteQuery = SimpleSQLiteQuery(queryString, args.toTypedArray())
        return estateDAO.getSearchEstate(query)
    }

}

