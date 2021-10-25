package com.openclassrooms.realestatemanager.ui.createAndEditEstate;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.databinding.ActivityAddPhotoItemBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private RequestManager glide;
    private List<Uri> mPhotoList = new ArrayList<Uri>();
    private LiveData<List<Uri>> mPhotoLiveDate;
    private List<String> mPhotoDescription;
    private long estateEdit;

    /**
     * Constructor
     *
     * @param glide
     * @param photoDescription
     * @param estateEdit
     */
    public PhotoAdapter( RequestManager glide, ArrayList<String> photoDescription, long estateEdit) {
        this.glide = glide;
        this.mPhotoDescription = photoDescription;
        this.estateEdit = estateEdit;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PhotoViewHolder(ActivityAddPhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    /**
     * Update viewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        //For photo description with photo
        Uri photoUri = null;
        String photoDescription = "";

        photoUri = mPhotoLiveDate.getValue().get(position);
        /*if (mPhotoList.size() > position) {
            photoUri = mPhotoList.get(position);
        }*/

        if (mPhotoDescription.size() > position) {
            photoDescription = mPhotoDescription.get(position);
        }
        try {
            holder.updateWithDetails(photoUri, this.glide, photoDescription, estateEdit);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * For return photos
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    /**
     * for set photolist in adapter
     *
     * @param photos
     */
    public void setPhotoList(List<Uri> photos) {
        mPhotoList.clear();
        mPhotoList.addAll(photos);
        notifyDataSetChanged();
    }

    public void setPhotoListLive(Uri photos){
        mPhotoLiveDate.getValue().addAll((Collection<? extends Uri>) photos) ;
        notifyDataSetChanged();
    }

    /**
     * For set photoDescription in adapter
     *
     * @param photoDescription
     */
    public void setPhotoDescription(List<String> photoDescription) {
        mPhotoDescription.clear();
        mPhotoDescription.addAll(photoDescription);
        notifyDataSetChanged();
    }
}
