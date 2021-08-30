package com.openclassrooms.realestatemanager.ui.master

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMasterBinding
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MasterFragment : Fragment() {

    private var fragmentListBinding: FragmentMasterBinding? = null
    // List
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MasterAdapter

    private val estateViewModel: EstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.fragment_master, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerViewEstate)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureViewModel()

    }

    private fun configureViewModel() {
        activity?.let { ViewModelProvider(it) }?.get(EstateViewModel::class.java)?.getEstates()
    }
}