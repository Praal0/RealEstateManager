package com.openclassrooms.realestatemanager.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.realestatemanager.databinding.FragmentMasterItemBinding;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.UriList;
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder>{

    //For data
    private List<Estate> estateList;
    private RequestManager glide;
    private List<UriList> photoLists;
    private LocationViewModel viewModel;
    private LifecycleOwner owner;

    /**
     * Constructor
     * @param estateList
     * @param glide
     * @param photoLists
     */
    public SearchResultAdapter(List<Estate> estateList, RequestManager glide, UriList photoLists,LifecycleOwner owner) {

        this.estateList = new ArrayList<>();
        this.glide = glide;
        this.photoLists = new ArrayList<>();
        this.owner = owner;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchResultViewHolder(FragmentMasterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.updateWithEstate(this.estateList.get(position), this.glide,viewModel,owner);
    }

    @Override
    public int getItemCount() {
        return estateList.size();
    }

    public Estate getEstates(int position) {
        return this.estateList.get(position);
    }

    /**
     * For update estates list
     *
     * @param estateList
     * @param locationViewModel
     */
    public void updateData(List<Estate> estateList, LocationViewModel locationViewModel,LifecycleOwner owner ) {
        viewModel = locationViewModel;
        this.estateList = estateList;
        this.notifyDataSetChanged();
    }
}
