package com.openclassrooms.realestatemanager.ui.createAndEditEstate

import android.app.PendingIntent.getActivity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.databinding.ActivityAddPhotoItemBinding
import android.provider.MediaStore

import android.graphics.Bitmap
import com.openclassrooms.realestatemanager.R


class PhotoAdapter() : RecyclerView.Adapter<PhotoViewHolder>() {

    private val glide: RequestManager? = null
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
            glide?.let { holder.updateWithDetails(photoUri, it, photoDescription, estateEdit) }
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
     * @param glide
     * @param photoDescription
     * @param estateEdit
     */
    fun updateWithDetails(photoList: Uri?, glide: RequestManager, photoDescription: String?, estateEdit: Long) {
        activityAddPhotoItemBinding?.photoDescription?.setText("photoDescription")
        activityAddPhotoItemBinding?.photoImage?.setImageResource(R.drawable.sold)
        //for delete image display in Edit and Not in create
        if (estateEdit == 0L) {
            activityAddPhotoItemBinding?.deleteImage?.visibility = View.INVISIBLE
        } else {
            activityAddPhotoItemBinding?.deleteImage?.visibility = View.VISIBLE
        }
    }

}
