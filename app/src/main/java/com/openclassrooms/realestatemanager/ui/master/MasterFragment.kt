package com.openclassrooms.realestatemanager.ui.master

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
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

    var test2 = Estate(
        2,
        "flat",
        200,
        4,
        2,
        1,
        200,
        1050.00,
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
        estateViewModel.insertEstates(test2)
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
        this.estateViewModel.getEstates().observe(viewLifecycleOwner, this::updateEstateList);
    }

    override fun onClickedEstate(estateId: Long) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        Navigation.createNavigateOnClickListener(R.id.detail_action, bundleOf("id" to estateId))
    }

    /**
     * for update estate list
     * @param estates
     */
    private fun updateEstateList(estates: List<Estate>?) {
        if (estates != null) adapter.updateData(estates)
    }


}