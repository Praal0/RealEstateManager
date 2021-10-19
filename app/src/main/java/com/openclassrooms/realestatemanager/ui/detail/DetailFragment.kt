package com.openclassrooms.realestatemanager.ui.detail

import android.content.ContentValues
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding
import com.openclassrooms.realestatemanager.models.PhotoDescription
import com.openclassrooms.realestatemanager.ui.createAndEditEstate.PhotoAdapter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.models.Estate


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
    private lateinit var adapter: PhotoAdapter
    private val photoText = PhotoDescription()
    private lateinit var positionMarker: Marker
    private var estateDetailId : Long? = 0
    private val estateEdit: Long = 0
    private var listPhoto : MutableList<Uri> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view: View = binding.root

        configureRecyclerView()

        //for lite map
        val options = GoogleMapOptions()
        options.liteMode(true)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    private fun configureRecyclerView() {
        adapter = PhotoAdapter(listPhoto, Glide.with(this), photoText.photoDescription, estateEdit)
        val horizontalLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhoto.layoutManager = horizontalLayoutManager
        binding.rvPhoto.adapter = adapter
        val intent = Intent(activity?.intent)
        estateDetailId = intent.getLongExtra("estate",0)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!Utils.isTablet(this.context)){
            estateDetailId?.let { viewModel.getEstateById(it).observe(viewLifecycleOwner, Observer {
                binding.etMandate.setText(it.numMandat.toString())
                binding.etMandate.isEnabled = false
                binding.etSurface.setText(it.surface.toString())
                binding.etSurface.isEnabled = false
                binding.etDescription.setText(it.description)
                binding.etDescription.isEnabled = false
                binding.etRooms.setText(it.rooms.toString())
                binding.etRooms.isEnabled = false
                binding.etBathrooms.setText(it.bathrooms.toString())
                binding.etBathrooms.isEnabled = false
                binding.etBedrooms.setText(it.bedrooms.toString())
                binding.etBedrooms.isEnabled = false
                binding.etAddress.isEnabled = false
                binding.etPostalCode.isEnabled = false
                binding.etCity.isEnabled = false
                binding.videoView.requestFocus()

                listPhoto.clear()
                if (it.photoList.photoList.isNotEmpty()){
                    for (photoStr in it.photoList.photoList) {
                        listPhoto.add(Uri.parse(photoStr))
                    }
                    adapter.setPhotoList(listPhoto)
                    adapter.setPhotoDescription(it.photoDescription.photoDescription)
                }
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
            }) }
        } else {
            setupObservers()
        }
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    private fun setupObservers() {
        viewModel.currentEstate.let { it ->
            it.observe(viewLifecycleOwner, Observer {
                binding.etMandate.setText(it.numMandat.toString())
                binding.etMandate.isEnabled = false
                binding.etSurface.setText(it.surface.toString())
                binding.etSurface.isEnabled = false
                binding.etDescription.setText(it.description)
                binding.etDescription.isEnabled = false
                binding.etRooms.setText(it.rooms.toString())
                binding.etRooms.isEnabled = false
                binding.etBathrooms.setText(it.bathrooms.toString())
                binding.etBathrooms.isEnabled = false
                binding.etBedrooms.setText(it.bedrooms.toString())
                binding.etBedrooms.isEnabled = false
                binding.etAddress.isEnabled = false
                binding.etPostalCode.isEnabled = false
                binding.etCity.isEnabled = false
                binding.videoView.requestFocus()


                listPhoto.clear()
                if (it.photoList.photoList.isNotEmpty()){
                    for (photoStr in it.photoList.photoList) {
                        listPhoto.add(Uri.parse(photoStr))
                    }
                    adapter.setPhotoList(listPhoto)
                    adapter.setPhotoDescription(it.photoDescription.photoDescription)
                }
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
            })} ?:run{Log.d("DetailFragment","Current estate : null")}

    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (Utils.isInternetAvailable(this.context)) {
            map = googleMap;
            map.uiSettings.isMyLocationButtonEnabled = false;
            map.uiSettings.isMapToolbarEnabled = false

            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.mapstyle))
                if (!success) {
                    Log.e(ContentValues.TAG, "Style parsing failed.")
                }
            }
            catch (e: Resources.NotFoundException) { Log.e(ContentValues.TAG, "Can't find style. Error: ", e) }
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15F))
        } else {
            Snackbar.make(binding.root, "No internet available", Snackbar.LENGTH_SHORT).show();
        }
        positionMarker()
    }

    private fun positionMarker() {
        estateDetailId?.let { it ->
            locationViewModel.getLocationById(it).observe(viewLifecycleOwner, Observer {
                if (it !=null){
                    binding.etAddress.setText(it.address)
                    binding.etCity.setText(it.city)
                    binding.etPostalCode.setText(it.zipCode)
                    if (Utils.isInternetAvailable(this.context)) {
                        map.clear()
                        val latLng = LatLng(it.latitude, it.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                        positionMarker = map.addMarker(MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                        positionMarker.showInfoWindow()

                    }
                }
            })
        }
    }



}