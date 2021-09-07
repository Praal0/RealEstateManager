package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "estate")
data class Estate(@PrimaryKey(autoGenerate = true) val id: Long,
                  var estateType:String?,
                  var surface: Int?,
                  var rooms:Int?,
                  var bedrooms:Int?,
                  var bathrooms:Int?,
                  var ground:Int?,
                  var price: Double?,
                  var description:String?,
                  var address:String?,
                  var postalCode:Int?,
                  var city:String?,
                  var schools:Boolean,
                  var stores:Boolean,
                  var park:Boolean,
                  var restaurants:Boolean,
                  var sold:Boolean,
                  var upOfSaleDate: Long?,
                  var soldDate:String?,
                  var agentName:String
                  ) : Serializable {
    constructor() : this(0, "", null, null, null, null, null, null, "", "",
        null, "", false, false, false, false, false, null, "", "")
}






