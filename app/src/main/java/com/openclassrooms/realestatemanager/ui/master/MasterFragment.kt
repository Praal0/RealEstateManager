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
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MasterFragment : Fragment() {

    private lateinit var binding: FragmentMasterBinding
    private lateinit var adapter: MasterAdapter
    private  var detailFragment: DetailFragment? = null
    val estateViewModel: EstateViewModel by viewModels()
    private var estateList: List<Estate>? = null

    companion object {
        fun newInstance() = MasterFragment()
    }

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
        configureOnClickRecyclerView()
    }

    private fun setupRecyclerView() {
        this.estateList = ArrayList()
        adapter = MasterAdapter(estateList as ArrayList<Estate>,Glide.with(this))
        binding.fragmentListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentListRV.adapter = adapter
    }

    private fun setupObservers() {
        this.estateViewModel.getEstates().observe(viewLifecycleOwner, this::updateEstateList)
    }


    /**
     * Configure item click on RecyclerView
     */
    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(binding.fragmentListRV, R.layout.fragment_master_item)
            .setOnItemClickListener { recyclerView, position, v ->
                detailFragment = DetailFragment()
                detailFragment = fragmentManager?.findFragmentById(R.id.detail_fragment_frameLayout) as DetailFragment?
                //for tablet format
                if (detailFragment?.isVisible == true) {
                    val estate: Estate = adapter.getEstateAt(position)
                    estateViewModel.selectItem(estate.numMandat)
                    detailFragment?.tablet = true
                    detailFragment?.updateUi(estate)
                } else {
                    //for phone format
                    val estate: Estate = adapter.getEstateAt(position)
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("estate", estate.numMandat)
                    startActivity(intent)
                }
            }
    }

    /**
     * for update estate list
     * @param estates
     */
    private fun updateEstateList(estates: List<Estate>?) {
        if (estates != null) adapter.updateData(estates, Glide.with(this),this)
    }
}