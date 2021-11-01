package com.openclassrooms.realestatemanager.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentSearchResultBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.SearchEstate
import com.openclassrooms.realestatemanager.models.UriList
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import com.openclassrooms.realestatemanager.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding
    private var estateList: List<Estate>? = null
    private val photoLists = UriList()
    private lateinit var mAdapter: SearchResultAdapter
    private val searchViewModel: SearchViewModel by viewModels()
    private val locationViewModel : LocationViewModel by viewModels()
    private var estateSearch: SearchEstate = SearchEstate()
    private var detailFragment: DetailFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val view: View = fragmentSearchResultBinding.root

        configureViewModel()
        configureRecyclerView()
        configureOnClickRecyclerView()
        return view
    }

    /**
     * For configure ViewModel
     */
    private fun configureViewModel() {
        val intent = Intent(activity?.intent)
        estateSearch = (intent.getSerializableExtra("estateSearch") as SearchEstate)
        Log.d("estateSearch", estateSearch.toString())
        //for observe data
        this.searchViewModel.searchEstate(estateSearch.estateType, estateSearch.city, estateSearch.minRooms, estateSearch.maxRooms,
            estateSearch.minSurface, estateSearch.maxSurface, estateSearch.minPrice, estateSearch.maxPrice,
            estateSearch.minUpOfSaleDate?.toLong(), estateSearch.maxOfSaleDate?.toLong(), estateSearch.photos,
            estateSearch.schools, estateSearch.stores, estateSearch.park, estateSearch.restaurants,
            estateSearch.sold).observe(viewLifecycleOwner, this::updateEstateList)
    }

    /**
     * Configuration RecyclerView
     * Configure RecyclerView, Adapter, LayoutManager & glue it
     */
    private fun configureRecyclerView() {
        this.estateList = ArrayList()
        //Create adapter
        this.mAdapter = SearchResultAdapter(this.estateList as ArrayList<Estate>, Glide.with(this), this.photoLists,this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        fragmentSearchResultBinding.searchResultListRV.layoutManager = layoutManager
        fragmentSearchResultBinding.searchResultListRV.adapter = mAdapter
    }

    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(fragmentSearchResultBinding.searchResultListRV, R.layout.fragment_master_item)
            .setOnItemClickListener { recyclerView, position, v ->
                val estate = mAdapter.getEstates(position)
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("estate", estate.numMandat)
                Log.d("bundleRV", "estate$estate")
                startActivity(intent)

            }
    }

    private fun updateEstateList(estates : List<Estate>) {
        mAdapter.updateData(estates,locationViewModel,this)
            Log.d("updateListSearch", "updateListSearch$estates")

        if (Objects.requireNonNull(estates).isEmpty()) {
            Snackbar.make(
                fragmentSearchResultBinding.root, "No result found, please retry with another search", Snackbar.LENGTH_LONG)
                .setAction("Return") { View.OnClickListener { activity?.finish() } }
                .show()
        }

        
    }


}