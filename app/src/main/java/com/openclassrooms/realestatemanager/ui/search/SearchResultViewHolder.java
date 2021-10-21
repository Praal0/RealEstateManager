package com.openclassrooms.realestatemanager.ui.search;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;

import java.util.Locale;
import java.util.Objects;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    FragmentMasterItemBinding fragmentMasterItemBinding;

    public SearchResultViewHolder(@NonNull FragmentMasterItemBinding FragmentMasterItemBinding) {
        super(FragmentMasterItemBinding.getRoot());
        this.fragmentMasterItemBinding = FragmentMasterItemBinding;
    }

    public void updateWithEstate(Estate estate, RequestManager glide) {
        if(estate != null) {
            // for Estate type
            Objects.requireNonNull(fragmentMasterItemBinding.estateType).setText(estate.getEstateType());
            // For city

            //for price
            if (estate.getPrice() != null) {
                Objects.requireNonNull(fragmentMasterItemBinding.price).setText(estate.getPrice().toString());
            }
            //for sold estate
            if (estate.getSold()) {
                fragmentMasterItemBinding.listPhotoSold.setImageResource(R.drawable.sold);
            }
            //for photo
            if (!estate.getPhotoList().getPhotoList().isEmpty()) {
                glide.load(estate.getPhotoList().getPhotoList().get(0)).into(fragmentMasterItemBinding.listPhoto);
            } else {
                fragmentMasterItemBinding.listPhoto.setImageResource(R.drawable.no_image);
            }
        }
    }
}
