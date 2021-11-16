package com.openclassrooms.realestatemanager.ui.master

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.common.primitives.UnsignedBytes.toInt
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.utils.Utils
import okhttp3.internal.Util
import java.text.NumberFormat
import java.util.Locale.*

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

    private val currencyFormat = NumberFormat.getCurrencyInstance()
    private lateinit var estate: Estate
    private var price : Int? = null

    @SuppressLint("SetTextI18n")
    fun bind(item: Estate,  glide: RequestManager) {
        this.estate = item
        itemBinding.city.text = item.locationEstate.city
        itemBinding.estateType.text = item.estateType
        if (getDefault() == FRANCE){
            price = item.price?.toInt()?.let { Utils.convertDollarToEuro(it) }
            itemBinding.price.text = currencyFormat.format(price)
        }else{
            itemBinding.price.text = currencyFormat.format(item.price)
        }

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
