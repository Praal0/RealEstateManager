package com.openclassrooms.realestatemanager.model

import android.content.ContentValues
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Estate(@PrimaryKey(autoGenerate = true)
                  var id: Long,
                  var estateType: String?,
                  var price: Double?,
                  var surface: Int?,
                  var roomNumber: Int?,
                  var bathroomNumber:Int?,
                  var bedroomNumber:Int?,
                  var desc: String?,
                  var parks:Boolean,
                  var shops:Boolean,
                  var schools:Boolean,
                  var highway:Boolean,
                  var estateStatute: String?,
                  var entryDate: Date,
                  var soldDate: Date?,
                  var estateAgent: String?){

    constructor() : this(0,"",null,null,null,
        null,null,"",false,false,
        false,false,"",Date(),null,"")

}