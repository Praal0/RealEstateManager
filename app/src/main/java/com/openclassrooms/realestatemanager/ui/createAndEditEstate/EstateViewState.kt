package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.net.Uri

class EstateViewState {
    val id : String = ""
    val name : String = ""
    val photos : List<PhotoViewState> = TODO()

    data class PhotoViewState(
        val id: String,
        val url: Uri
    )
}

