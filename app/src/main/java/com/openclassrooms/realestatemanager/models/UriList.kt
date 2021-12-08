package com.openclassrooms.realestatemanager.models

import java.io.Serializable

data class UriList(
    val uriList: java.util.ArrayList<String> = ArrayList()
) : Serializable
