package com.openclassrooms.realestatemanager.ui.master

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding
import com.openclassrooms.realestatemanager.models.Estate

class MasterAdapter(estateList : List<Estate>, glide : RequestManager) :RecyclerView.Adapter<MasterViewHolder>() {


    private lateinit var owner: LifecycleOwner

    private var items = ArrayList<Estate>()
    private var glide : RequestManager = glide

    /**
     * For update estate list
     *
     * @param estateList
     */
    fun updateData(estateList: List<Estate?>, glide : RequestManager,owner: LifecycleOwner) {
        items = estateList as ArrayList<Estate>
        this.glide = glide
        this.owner = owner

        notifyDataSetChanged()
    }

    fun getEstateAt(position: Int): Estate {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        return MasterViewHolder(FragmentMasterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),owner)
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) {
        holder.bind(items[position],this.glide)
        holder.itemView.setOnClickListener {
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int = items.size
}

class MasterViewHolder(private val itemBinding: FragmentMasterItemBinding,private val owner: LifecycleOwner) : RecyclerView.ViewHolder(itemBinding.root){

    private lateinit var estate: Estate

    @SuppressLint("SetTextI18n")
    fun bind(item: Estate,  glide: RequestManager) {
        this.estate = item
        itemBinding.city.text = item.locationEstate.city
        itemBinding.estateType.text = item.estateType
        itemBinding.price.text = "$"+item.price.toString()
        //for estate sold
        if (estate.sold) {
            glide.load(R.drawable.sold).apply(RequestOptions.centerCropTransform()).into( itemBinding.listPhotoSold)
        }else {
            itemBinding.listPhotoSold.setImageResource(0)
        }
        //for photo
        if(estate.photoList.photoList.isNotEmpty()) {
            glide.load(estate.photoList.photoList[0]).apply(RequestOptions.centerCropTransform()).into(itemBinding.listPhoto)
        }else {
            glide.load(R.drawable.no_image).apply(RequestOptions.centerCropTransform()).into(itemBinding.listPhoto)

        }
    }

}
