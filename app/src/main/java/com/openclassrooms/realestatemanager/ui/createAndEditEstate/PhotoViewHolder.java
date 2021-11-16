package com.openclassrooms.realestatemanager.ui.createAndEditEstate;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPhotoItemBinding;

import java.util.Objects;

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private ActivityAddPhotoItemBinding activityAddPhotoItemBinding;

    public PhotoViewHolder(ActivityAddPhotoItemBinding activityAddPhotoItemBinding) {
        //for viewBinding
        super(activityAddPhotoItemBinding.getRoot());
        this.activityAddPhotoItemBinding = activityAddPhotoItemBinding;
    }

    /**
     * For update with data
     *
     * @param photoList
     * @param glide
     * @param photoDescription
     */
    public void updateWithDetails(Uri photoList, RequestManager glide, String photoDescription,Boolean detail) {
        activityAddPhotoItemBinding.photoDescription.setText(photoDescription);
        activityAddPhotoItemBinding.deleteImage.setVisibility(View.VISIBLE);
        glide.load(photoList).apply(RequestOptions.centerCropTransform()).into(activityAddPhotoItemBinding.photoImage);

        if (detail){
            Objects.requireNonNull(activityAddPhotoItemBinding.deleteImage).setVisibility(View.INVISIBLE);
        }else{
            Objects.requireNonNull(activityAddPhotoItemBinding.deleteImage).setVisibility(View.VISIBLE);
        }

    }
}
