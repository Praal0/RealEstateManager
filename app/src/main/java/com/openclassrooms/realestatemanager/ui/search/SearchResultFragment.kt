package com.openclassrooms.realestatemanager.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentSearchResultBinding
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment

import com.openclassrooms.realestatemanager.models.UriList

import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.SearchEstate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var fragmentSearchResultBinding: FragmentSearchResultBinding
    private var estateList: List<Estate>? = null
    private val photoLists = UriList()
    private var mAdapter: SearchResultAdapter? = null
    private val estateViewModel: EstateViewModel by viewModels()
    private val estateSearch: SearchEstate? = null
    private val detailFragment: DetailFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val view: View = fragmentSearchResultBinding.root
        return view
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
        fragmentSearchResultBinding.searchResultListRV.setLayoutManager(layoutManager)
        fragmentSearchResultBinding.searchResultListRV.setAdapter(mAdapter)
    }



}