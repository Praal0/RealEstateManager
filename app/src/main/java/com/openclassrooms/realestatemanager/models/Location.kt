package com.openclassrooms.realestatemanager.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity()
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id:Long,
                    var longitude : Double,
                    var latitude : Double,
                    var address:String?,
                    var city:String?,
                    var zipCode:String?,
                    var estateId:Long)