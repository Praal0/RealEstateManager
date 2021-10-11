package com.openclassrooms.realestatemanager.ui.detail

import android.content.ContentValues
import android.content.res.Resources
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.maps.MapView
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.*
import com.openclassrooms.realestatemanager.R

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class DetailFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: EstateViewModel by viewModels()
    private val locationViewModel : LocationViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var positionMarker: Marker
    private var estateDetailId : Long? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view: View = binding.root

        //for lite map
        val options = GoogleMapOptions()
        options.liteMode(true)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    private fun setupObservers() {
        val intentDetail = this.activity?.intent
         estateDetailId = intentDetail?.getLongExtra("estate",0)
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
        if (Utils.isInternetAvailable(this.context)) {
            map = googleMap;
            map.uiSettings.isMyLocationButtonEnabled = false;
            map.uiSettings.isMapToolbarEnabled = false
            positionMarker()
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this.context, R.raw.mapstyle))
                if (!success) {
                    Log.e(ContentValues.TAG, "Style parsing failed.")
                }
            }
            catch (e: Resources.NotFoundException) { Log.e(ContentValues.TAG, "Can't find style. Error: ", e) }
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(14F))
        } else {
            Snackbar.make(binding.root, "No internet available", Snackbar.LENGTH_SHORT).show();
        }
    }

    private fun positionMarker() {
        estateDetailId?.let { it ->
            locationViewModel.getLocationById(it).observe(viewLifecycleOwner, Observer {
                if (it !=null){
                    map.clear()
                    val latLng = LatLng(it.latitude, it.longitude)
                    positionMarker = map.addMarker(MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                    positionMarker.showInfoWindow()
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            })
        }
    }



}