package com.openclassrooms.realestatemanager.ui.master

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel

class MasterAdapter(private val result: MasterItemListener) :RecyclerView.Adapter<MasterViewHolder>() {

    private lateinit var viewModel : LocationViewModel

    interface MasterItemListener{
        fun onClickedEstate(EstateId: Long)
    }

    private var items = ArrayList<Estate>()

    /**
     * For update estate list
     *
     * @param estateList
     */
    fun updateData(estateList: List<Estate?>, locationViewModel: LocationViewModel) {
        items = estateList as ArrayList<Estate>
        this.viewModel = locationViewModel
        notifyDataSetChanged()
    }

    fun getEstateAt(position: Int): Estate {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        val binding: FragmentMasterItemBinding = FragmentMasterItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)

        return MasterViewHolder(binding, result)
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) { holder.bind(items[position]) }

    override fun getItemCount(): Int = items.size

}

class MasterViewHolder(private val itemBinding: FragmentMasterItemBinding, private val listener: MasterAdapter.MasterItemListener)
    : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

    private lateinit var estate: Estate

    init { itemBinding.root.setOnClickListener(this) }

    @SuppressLint("SetTextI18n")
    fun bind(item: Estate) {
        this.estate = item
        itemBinding.city.text = "TEST"
        itemBinding.estateType.text = item.estateType
        itemBinding.price.text = "$"+item.price.toString()
        //for estate sold
        if (estate.sold) {
            itemBinding.listPhotoSold.setImageResource(R.drawable.sold)
        }else {
            itemBinding.listPhotoSold.setImageResource(0)
        }
    }

    override fun onClick(v: View?) {
        listener.onClickedEstate(estate.numMandat)
    }

}
