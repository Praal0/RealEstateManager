package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "picture",foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
    data class Picture(@PrimaryKey(autoGenerate = true) val id:Int,
                       var imagePath: String,
                       var imageTitle:String?,
                       var imageDesc:String?,
                       var estateId:Long){

    }
