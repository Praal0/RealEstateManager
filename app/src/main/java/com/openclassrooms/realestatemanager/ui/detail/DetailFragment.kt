package com.openclassrooms.realestatemanager.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: EstateDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong("id")?.let { viewModel.start(it) }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.estate.observe(viewLifecycleOwner, Observer {
            binding.etMandate.setText(it.id.toString());
            binding.etMandate.isEnabled = false
            binding.etSurface.setText(it.surface.toString());
            binding.etSurface.isEnabled = false
            binding.etDescription.setText(it.description)
            binding.etDescription.isEnabled = false
            binding.etRooms.setText(it.rooms.toString())
            binding.etRooms.isEnabled = false
            binding.etBathrooms.setText(it.bathrooms.toString())
            binding.etBathrooms.isEnabled = false
            binding.etBedrooms.setText(it.bedrooms.toString())
            binding.etBedrooms.isEnabled = false
            binding.etAddress.setText(it.address)
            binding.etAddress.isEnabled = false
            binding.etPostalCode.setText(it.postalCode.toString())
            binding.etPostalCode.isEnabled = false
            binding.etCity.setText(it.city)
            binding.etCity.isEnabled = false
        })
    }




}