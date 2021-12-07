package com.openclassrooms.realestatemanager.models

import android.content.ContentValues
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "estate")
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
                  @Embedded
                  var locationEstate : Location
                  ) : Serializable{
    constructor() : this(0, "", null, null, null, null, null, null, "",
        false, false, false, false, false, "", "", "",
        UriList(), PhotoDescription(), UriList(), Location(0.0,0.0,"","",null))

    /**
     * For ContentProvider
     */
    companion object{
        fun fromContentValues(values: ContentValues): Estate {
            val estate = Estate()

            if (values.containsKey("estateType")) estate.estateType = values.getAsString("estateType")
            if (values.containsKey("surface")) estate.surface = values.getAsInteger("surface")
            if (values.containsKey("rooms")) estate.rooms = values.getAsInteger("rooms")
            if (values.containsKey("bedrooms")) estate.bedrooms = values.getAsInteger("bedrooms")
            if (values.containsKey("bathrooms")) estate.bathrooms = values.getAsInteger("bathrooms")
            if (values.containsKey("ground")) estate.ground = values.getAsInteger("ground")
            if (values.containsKey("price")) estate.price = values.getAsDouble("price")
            if (values.containsKey("description")) estate.description = values.getAsString("description")
            if (values.containsKey("address")) estate.locationEstate.address = values.getAsString("address")
            if (values.containsKey("zipCode")) estate.locationEstate.zipCode = values.getAsString("zipCode")
            if (values.containsKey("city")) estate.locationEstate.city = values.getAsString("city")
            if (values.containsKey("schools")) estate.schools = values.getAsBoolean("schools")
            if (values.containsKey("stores")) estate.stores = values.getAsBoolean("stores")
            if (values.containsKey("park")) estate.park = values.getAsBoolean("park")
            if (values.containsKey("restaurants")) estate.restaurants = values.getAsBoolean("restaurants")
            if (values.containsKey("upOfSaleDate")) estate.upOfSaleDate = values.getAsString("upOfSaleDate")
            if (values.containsKey("soldDate")) estate.soldDate = values.getAsString("soldDate")
            if (values.containsKey("agentName")) estate.agentName = values.getAsString("agentName")

            return estate
        }
    }


}








