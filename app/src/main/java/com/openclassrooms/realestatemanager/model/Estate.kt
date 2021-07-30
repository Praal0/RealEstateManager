package com.openclassrooms.realestatemanager.model

import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

class Estate (@PrimaryKey(autoGenerate = true) var id: Long,
              var type : String,
              var surface : Int,
              var numberPieces : Int,
              var numberBathrooms : Int,
              var numberBedrooms : Int,
              var description : String,
              var adresse: String,
              var interestPoint : String,
              var entryDate : Date,
              var sellDate  : Date,
              var estateAgent: String?)  {

    constructor() : this(0,"",0,0,0,
        0,"","","",
        Date(),Date(),"")
}