package com.openclassrooms.realestatemanager.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import java.util.*
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import com.openclassrooms.realestatemanager.models.Estate





/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: EstateViewModel by viewModels()
    private var estateDetailId: Long = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view: View = binding.getRoot()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        val intentDetail = this.activity?.intent
        val estateDetailId = intentDetail?.getLongExtra("estate",0)
        Log.d("estateDetailId", "estateDetailId$estateDetailId")
        estateDetailId?.let {
            viewModel.getEstate(it).observe(viewLifecycleOwner, Observer {
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




}