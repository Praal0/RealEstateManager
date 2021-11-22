package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.net.Uri

class EstateViewState {
    val id : String = ""
    val name : String = ""
    val photos : List<PhotoViewState> = ArrayList()

    data class PhotoViewState(
        val id: String,
        val url: Uri
    )
}

