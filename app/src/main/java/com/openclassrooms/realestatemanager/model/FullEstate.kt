package com.openclassrooms.realestatemanager.model

import androidx.room.Embedded
import androidx.room.Relation

class FullEstate {
    @Embedded
    lateinit var estate:Estate

    @Relation(parentColumn = "id",
        entityColumn = "estateId")
    var images:List<Image.Image> = arrayListOf()

    @Embedded
    lateinit var location:Location
}