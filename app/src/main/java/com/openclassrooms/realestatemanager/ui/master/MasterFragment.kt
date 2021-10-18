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
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.ItemTouchHelper

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

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return false }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.getEstateAt(viewHolder.absoluteAdapterPosition)?.let {
                    estateViewModel.deleteEstate(it.numMandat)
                }
            }
        }).attachToRecyclerView(binding.fragmentListRV)
    }

    private fun setupObservers() {
        this.estateViewModel.getEstates().observe(viewLifecycleOwner, this::updateEstateList)
    }

    override fun onClickedEstate(estateId: Long) {
        estateViewModel.setCurrentEstate(estateId)
        if (!Utils.isTablet(this.context)){
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("estate", estateId)
            Log.d("bundleRV", "estate$estateId")
            startActivity(intent)
        }else{
            Log.d("bundleListFragment", "bundleFragment$estateId")
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("estate", estateId)
            startActivity(intent)
        }
    }

    /**
     * for update estate list
     * @param estates
     */
    private fun updateEstateList(estates: List<Estate>?) {
        if (estates != null) adapter.updateData(estates,locationViewModel)
    }
}