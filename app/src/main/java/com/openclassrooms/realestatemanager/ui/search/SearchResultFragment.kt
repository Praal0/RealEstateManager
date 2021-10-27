package com.openclassrooms.realestatemanager.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.FragmentSearchResultBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.SearchEstate
import com.openclassrooms.realestatemanager.models.UriList
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding
    private var estateList: List<Estate>? = null
    private val photoLists = UriList()
    private var mAdapter: SearchResultAdapter? = null
    private val searchViewModel: SearchViewModel by viewModels()
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
        this.mAdapter = SearchResultAdapter(this.estateList, Glide.with(this), this.photoLists)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        fragmentSearchResultBinding.searchResultListRV.layoutManager = layoutManager
        fragmentSearchResultBinding.searchResultListRV.adapter = mAdapter
    }

    private fun configureOnClickRecyclerView() {

    }

    private fun updateEstateList( estates : List<Estate>) {
        
    }


}