package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "estate")
data class Estate(@PrimaryKey(autoGenerate = true) val id: Int,
                  var estateType: String?,
                  var price: Double?,
                  var surface: Int?,
                  var roomNumber: Int?,
                  var bathroomNumber:Int?,
                  var bedroomNumber:Int?,
                  var desc: String?,
                  var address:String?,
                  var postalCode:Int?,
                  var city:String?,
                  var parks:Boolean,
                  var shops:Boolean,
                  var schools:Boolean,
                  var highway:Boolean,
                  var estateStatute: String?,
                  var estateAgent: String?){

}



