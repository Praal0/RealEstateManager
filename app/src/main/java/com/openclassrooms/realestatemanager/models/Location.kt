package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
    parentColumns = ["numMandat"],
    childColumns = ["estateId"])])
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "locationId") var id:Long,
                    var longitude : Double?,
                    var latitude : Double?,
                    var address:String?,
                    var city:String?,
                    var zipCode:String?,
                    var estateId:Long)