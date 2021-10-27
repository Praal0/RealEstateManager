package com.openclassrooms.realestatemanager.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "estate",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = ["id"],
        childColumns = ["locationId"])]
)
data class Estate(@PrimaryKey(autoGenerate = true) var numMandat:Long,
                  var estateType:String?,
                  var surface: Int?,
                  var rooms:Int?,
                  var bedrooms:Int?,
                  var bathrooms:Int?,
                  var ground:Int?,
                  var price: Double?,
                  var description:String?,
                  var schools:Boolean,
                  var stores:Boolean,
                  var park:Boolean,
                  var restaurants:Boolean,
                  var sold:Boolean,
                  var upOfSaleDate: String?,
                  var soldDate:String?,
                  var agentName:String?,
                  var photoList: UriList,
                  var photoDescription : PhotoDescription,
                  var video:UriList,
                  var locationId : Long
                  ) : Serializable {

    constructor() : this(0,"",0,0,0,0,0,null,"",false,false,
        false,false,false,"","","",UriList(), PhotoDescription(), UriList(),0)
     }








