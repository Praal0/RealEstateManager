package com.openclassrooms.realestatemanager.ui.master

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding
import com.openclassrooms.realestatemanager.models.Estate

class MasterAdapter(private val result: MasterItemListener) :RecyclerView.Adapter<MasterViewHolder>() {


    interface MasterItemListener{
        fun onClickedCharacter(characterId: Long)
    }

    private val items = ArrayList<Estate>()

    fun setItems(items: ArrayList<Estate>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterViewHolder {
        val binding: FragmentMasterItemBinding = FragmentMasterItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return MasterViewHolder(binding, result)
    }

    override fun onBindViewHolder(holder: MasterViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}

class MasterViewHolder(private val itemBinding: FragmentMasterItemBinding, private val listener: MasterAdapter.MasterItemListener)
    : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

    private lateinit var estate: Estate

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: Estate) {
        this.estate = item
        itemBinding.city.text = item.city
        itemBinding.estateType.text = item.estateType
        itemBinding.price.text = item.price.toString()
    }

    override fun onClick(v: View?) {
        listener.onClickedCharacter(estate.id)
    }

}
