package com.openclassrooms.realestatemanager.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MasterFragment : Fragment(), MasterAdapter.MasterItemListener {

    private lateinit var binding: FragmentMasterBinding
    private lateinit var adapter: MasterAdapter
    private val estateViewModel: EstateViewModel by viewModels()
    var test = Estate(
        1,
        "house",
        200,
        4,
        2,
        1,
        200,
        100000.00,
        "Très belle maison",
        "2 rue du Pont",
        66000,
        "Perpignan",
        true,
        false,
        false,
        true,
        true,
        1601510400000L,
        "",
        "Karine Danjard"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMasterBinding.inflate(inflater, container, false)
        estateViewModel.insertEstates(test)
        val view: View = binding.getRoot()
        return view
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
        estateViewModel.getEstates().observe(viewLifecycleOwner, Observer {
             adapter.setItems(ArrayList(it.size))
        })
    }

    override fun onClickedEstate(estateId: Long) {
        findNavController().navigate(
            R.id.action_masterFragment_to_detailFragment,
            bundleOf("id" to estateId)
        )
    }


}