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
import android.net.Uri
import android.util.Log
import android.view.View.INVISIBLE
import android.widget.MediaController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.maps.MapView
import io.reactivex.disposables.Disposable


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: EstateViewModel by viewModels()
    private val mapView: MapView? = null
    private var map: GoogleMap? = null
    private val mDisposable: Disposable? = null
    private val completeAddress: String? = null

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
            viewModel.getEstateById(it).observe(viewLifecycleOwner, Observer {
                binding.etMandate.setText(it.numMandat.toString());
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
                binding.videoView.requestFocus()
                if (it.video.photoList.isNotEmpty()){
                    for (videoStr in it.video.photoList) {
                        binding.videoView.setVideoURI(Uri.parse(videoStr))
                        val mediaController = MediaController(this.context)
                        binding.videoView.setMediaController(mediaController)
                        mediaController.setAnchorView(binding.videoView)
                        binding.videoView.start()
                    }
                }else{
                    binding.videoView.visibility = INVISIBLE
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

    }


}