package com.openclassrooms.realestatemanager.ui.master

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

class MasterAdapter(private val result :List<Estate>) :RecyclerView.Adapter<MasterViewHolder>() {


    // FOR DATA
    private val mResults: List<Estate>? = result


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.fragment_master_item, parent, false)
        return MasterViewHolder(view)
    }

    fun getEstate(position: Int): Estate? {
        return mResults?.get(position)
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        mResults?.get(position)?.let{ holder.updateWithData(it) }
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        if (mResults != null) itemCount = mResults.size
        return itemCount
    }
}
