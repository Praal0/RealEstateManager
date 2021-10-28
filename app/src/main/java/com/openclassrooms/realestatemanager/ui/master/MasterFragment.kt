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
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailFragment
import com.openclassrooms.realestatemanager.ui.main.MainActivity
import com.openclassrooms.realestatemanager.utils.ItemClickSupport
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.openclassrooms.realestatemanager.models.UriList





@AndroidEntryPoint
class MasterFragment : Fragment() {

    private lateinit var binding: FragmentMasterBinding
    private lateinit var adapter: MasterAdapter
    private lateinit var detailFragment: DetailFragment
    val estateViewModel: EstateViewModel by viewModels()
    val locationViewModel : LocationViewModel by viewModels()
    private var estateList: List<Estate>? = null

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
        ItemClickSupport.addTo(binding.fragmentListRV, com.openclassrooms.realestatemanager.R.layout.fragment_master_item)
            .setOnItemClickListener { recyclerView, position, v ->
                detailFragment = fragmentManager?.findFragmentById(com.openclassrooms.realestatemanager.R.id.detail_fragment_frameLayout) as DetailFragment
                //for tablet format
                if (detailFragment != null && detailFragment.isVisible) {
                    val estate: Estate = adapter.getEstateAt(position)
                    Log.d("bundleListFragment", "bundleFragment$estate")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("estate", estate.numMandat)
                    startActivity(intent)
                } else {
                    //for phone format
                    val estate: Estate = adapter.getEstateAt(position)
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("estate", estate.numMandat)
                    Log.d("bundleRV", "estate$estate")
                    startActivity(intent)
                }
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