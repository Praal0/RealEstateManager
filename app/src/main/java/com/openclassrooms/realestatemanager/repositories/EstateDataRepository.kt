package com.openclassrooms.realestatemanager.repositories

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

    fun gesEstateByID(estateID:Long) : LiveData<Estate>{
        return this.estateDAO.getEstate(estateID)
    }

    /**
     * Create
     *
     * @param estate
     */
    // --- CREATE ---
    fun createEstate(estate: Estate) {
        estateDAO.insertEstate(estate)
    }

    /**
     * Delete
     *
     * @param mandateEstateID
     * */
    fun deleteEstate(mandateEstateID: Long) {
        estateDAO.deleteItem(mandateEstateID)
    }


    /**
     * Update
     *
     * @param estate
     */
    // --- UPDATE ---
    fun updateEstate(estate: Estate) {
        estateDAO.updateEstate(estate)
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

