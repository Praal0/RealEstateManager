package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.openclassrooms.realestatemanager.databinding.ActivityAddPhotoItemBinding


class PhotoAdapter(
    listPhoto: MutableList<Uri>?,
    with: RequestManager,
    photoDescription: java.util.ArrayList<String>,
    estateEdit: Long
) : RecyclerView.Adapter<PhotoViewHolder>() {

    private val mPhotoList: MutableList<Uri> = ArrayList()
    private val mPhotoDescription: MutableList<String> = ArrayList()
    private val estateEdit: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding: ActivityAddPhotoItemBinding = ActivityAddPhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //For photo description with photo
        var photoUri: Uri? = null
        var photoDescription = ""
        if (mPhotoList.size > position) {
            photoUri = mPhotoList[position]
        }
        if (mPhotoDescription.size > position) {
            photoDescription = mPhotoDescription[position]
        }
        try {
            holder.updateWithDetails(photoUri, photoDescription, estateEdit)
            notifyItemChanged(position);
        } catch (e: Exception) {
            e.message
        }
    }

    override fun getItemCount(): Int {
        return mPhotoList.size
    }

    /**
     * for set photolist in adapter
     *
     * @param photos
     */
    fun setPhotoList(photos: Collection<Uri>) {
        mPhotoList.clear()
        mPhotoList.addAll(photos)
        this.notifyDataSetChanged()
    }

    /**
     * For set photoDescription in adapter
     *
     * @param photoDescription
     */
    fun setPhotoDescription(photoDescription: Collection<String>) {
        mPhotoDescription.clear()
        mPhotoDescription.addAll(photoDescription)
        this.notifyDataSetChanged()
    }

}

class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val activityAddPhotoItemBinding: ActivityAddPhotoItemBinding? = null
    /**
     * For update with data
     *
     * @param photoList
     * @param photoDescription
     * @param estateEdit
     */
    fun updateWithDetails(photoList: Uri?, photoDescription: String?, estateEdit: Long) {
        activityAddPhotoItemBinding?.photoDescription?.setText(photoDescription)
        activityAddPhotoItemBinding?.photoImage?.setImageURI(photoList)

    }

}
