package com.openclassrooms.realestatemanager.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.models.Estate
import javax.inject.Inject


class EstateDataRepository @Inject constructor(
    private val estateDAO: EstateDAO
    ) {
    fun getEstates(): LiveData<List<Estate>> {
        return this.estateDAO.getEstates()
    }

    fun getEstateByID(estateID:Long) : LiveData<Estate>{
        return this.estateDAO.getEstate(estateID)
    }

    /**
     * Create
     *
     * @param estate
     */
    // --- CREATE ---
    suspend fun createEstate(estate: Estate) {
        try {
            estateDAO.insertEstate(estate)
        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("EstateDataRepository","Cannot Insert : $cause")
        }

    }

    /**
     * Delete
     *
     * @param mandateEstateID
     * */
    suspend fun deleteEstate(mandateEstateID: Long) {
        try {
            estateDAO.deleteItem(mandateEstateID)
        }catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("EstateDataRepository", "Cannot Delete : $cause")
        }

    }


    /**
     * Update
     *
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
     *
     * @param queryString
     * @param args
     * @return
     */
    fun getSearchEstate(queryString: String?, args: List<Any?>): LiveData<List<Estate>> {
        val query: SupportSQLiteQuery = SimpleSQLiteQuery(queryString, args.toTypedArray())
        return estateDAO.getSearchEstate(query)
    }

}

/**
 * Thrown when there was a error fetching a new title
 *
 * @property message user ready error message
 * @property cause the original cause of this exception
 */
class TitleRefreshError(message: String, cause: Throwable?) : Throwable(message, cause)

