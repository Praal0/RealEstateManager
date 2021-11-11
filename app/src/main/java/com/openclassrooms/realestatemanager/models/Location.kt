package com.openclassrooms.realestatemanager.models


data class Location(var longitude : Double,
                    var latitude : Double,
                    var address:String?,
                    var city:String?,
                    var zipCode:String?)