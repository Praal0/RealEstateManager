package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

class Image {
    @Entity(tableName = "image",foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
    data class Image(@PrimaryKey(autoGenerate = true) val id:Long,
                     var imagePath: String,
                     var imageTitle:String?,
                     var imageDesc:String?,
                     var estateId:Long
    )
}