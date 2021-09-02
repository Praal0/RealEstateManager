package com.openclassrooms.realestatemanager.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MasterFragment : Fragment(), MasterAdapter.MasterItemListener {

    private lateinit var binding: FragmentMasterBinding
    private lateinit var adapter: MasterAdapter
    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        binding.recyclerViewEstate.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewEstate.adapter = adapter
    }

    private fun setupObservers() {
        estateViewModel.estates.observe(viewLifecycleOwner, Observer {
             adapter.setItems(ArrayList(it.size))

        })
    }


    override fun onClickedCharacter(characterId: Long) {

    }


}