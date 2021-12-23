package com.openclassrooms.realestatemanager.ui.search;

import static java.util.Locale.FRANCE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.text.NumberFormat;
import java.util.Locale;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.utils.Utils;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    FragmentMasterItemBinding fragmentMasterItemBinding;
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public SearchResultViewHolder(@NonNull FragmentMasterItemBinding FragmentMasterItemBinding) {
        super(FragmentMasterItemBinding.getRoot());
        this.fragmentMasterItemBinding = FragmentMasterItemBinding;
    }

    public void updateWithEstate(Estate estate, RequestManager glide, LifecycleOwner owner) {
        if(estate != null) {
            // for Estate type
           fragmentMasterItemBinding.estateType.setText(estate.getEstateType());

           fragmentMasterItemBinding.city.setText(estate.getLocationEstate().getCity());

            //for price
            if (Locale.getDefault().getLanguage() == "fr") {
                int price = Utils.convertDollarToEuro(estate.getPrice().intValue());
                fragmentMasterItemBinding.price.setText(currencyFormat.format(price));
            }else{
                fragmentMasterItemBinding.price.setText(currencyFormat.format(estate.getPrice()));
            }

            //for sold estate
            if (estate.getSold()) {
                glide.load(R.drawable.sold).apply(RequestOptions.centerCropTransform()).into( fragmentMasterItemBinding.listPhotoSold);
            }
            //for photo
            if (!estate.getPhotoList().getUriList().isEmpty()) {
                glide.load(estate.getPhotoList().getUriList().get(0)).apply(RequestOptions.centerCropTransform()).into(fragmentMasterItemBinding.listPhoto);
            } else {
                glide.load(R.drawable.no_image).apply(RequestOptions.centerCropTransform()).into(fragmentMasterItemBinding.listPhoto);
            }
        }
    }
}
