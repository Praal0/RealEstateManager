package com.openclassrooms.realestatemanager.ui.master

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint

import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.ui.main.MainActivity

@AndroidEntryPoint
class MasterFragment : Fragment(), MasterAdapter.MasterItemListener {

    private lateinit var binding: FragmentMasterBinding
    private lateinit var adapter: MasterAdapter
    val estateViewModel: EstateViewModel by viewModels()
    val locationViewModel : LocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMasterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = MasterAdapter(this)
        binding.fragmentListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentListRV.adapter = adapter
    }

    private fun setupObservers() {
        this.estateViewModel.getEstates().observe(viewLifecycleOwner, this::updateEstateList)
    }

    override fun onClickedEstate(estate: Estate) {
        //estateViewModel.setCurrentEstate(estate)
        if (!Utils.isTablet(this.context)){
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("estate", estate.numMandat)
            Log.d("bundleRV", "estate$estate")
            startActivity(intent)
        }else{
            Log.d("bundleListFragment", "bundleFragment$estate")
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("estateId", estate.numMandat)
            startActivity(intent)
        }
    }

    /**
     * for update estate list
     * @param estates
     */
    private fun updateEstateList(estates: List<Estate>?) {
        if (estates != null) adapter.updateData(estates,locationViewModel, Glide.with(this),this)
    }
}