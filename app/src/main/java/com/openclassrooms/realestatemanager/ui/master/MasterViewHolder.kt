package com.openclassrooms.realestatemanager.ui.master

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.model.Estate

class MasterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var listPhoto : ImageView = itemView.findViewById(R.id.list_photo)
    private var photoSold : ImageView = itemView.findViewById(R.id.list_photo_sold)
    private var estateType : TextView = itemView.findViewById(R.id.estateType)
    private var city : TextView = itemView.findViewById(R.id.city)
    private var price : TextView = itemView.findViewById(R.id.price)


    fun updateWithData(estate: Estate) {
        city.text = estate.city
        price.text = estate.price.toString()
        estateType.text = estate.estateType
    }
}