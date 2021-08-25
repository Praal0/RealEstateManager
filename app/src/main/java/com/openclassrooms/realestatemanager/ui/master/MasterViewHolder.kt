package com.openclassrooms.realestatemanager.ui.master

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.model.Estate

class MasterViewHolder(itemView: View,val binding: FragmentMasterBinding) : RecyclerView.ViewHolder(itemView) {

    val listPhoto : AppCompatImageView? = null
    val photoSold : AppCompatImageView? = null
    val estateType : AppCompatEditText? = null
    val city : AppCompatEditText? = null
    val price : AppCompatEditText? = null



    fun updateWithData(estate: Estate) {

    }
}