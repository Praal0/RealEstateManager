package com.openclassrooms.realestatemanager.ui.search;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.Location;
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel;

import java.security.acl.Owner;
import java.util.Locale;
import java.util.Objects;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    FragmentMasterItemBinding fragmentMasterItemBinding;

    public SearchResultViewHolder(@NonNull FragmentMasterItemBinding FragmentMasterItemBinding) {
        super(FragmentMasterItemBinding.getRoot());
        this.fragmentMasterItemBinding = FragmentMasterItemBinding;
    }

    public void updateWithEstate(Estate estate, RequestManager glide, LocationViewModel locationViewModel, LifecycleOwner owner) {
        if(estate != null) {
            // for Estate type
           fragmentMasterItemBinding.estateType.setText(estate.getEstateType());

           fragmentMasterItemBinding.city.setText(estate.getLocationEstate().getCity());

            //for price
            if (estate.getPrice() != null) {
                fragmentMasterItemBinding.price.setText(estate.getPrice().toString());
            }

            //for sold estate
            if (estate.getSold()) {
                glide.load(R.drawable.sold).apply(RequestOptions.centerCropTransform()).into( fragmentMasterItemBinding.listPhotoSold);
            }
            //for photo
            if (!estate.getPhotoList().getPhotoList().isEmpty()) {
                glide.load(estate.getPhotoList().getPhotoList().get(0)).into(fragmentMasterItemBinding.listPhoto);
            } else {
                glide.load(R.drawable.no_image).apply(RequestOptions.centerCropTransform()).into(fragmentMasterItemBinding.listPhoto);
            }
        }
    }
}
