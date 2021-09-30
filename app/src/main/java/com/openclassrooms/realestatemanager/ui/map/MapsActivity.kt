package com.openclassrooms.realestatemanager.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import pub.devrel.easypermissions.EasyPermissions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mPosition : String
    private lateinit var binding: ActivityMapsBinding
    private val map: GoogleMap? = null
    private lateinit var mMapView: MapView
    private val estateViewModel: EstateViewModel by viewModels()
    private val perms = ("Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (EasyPermissions.hasPermissions(this, perms)){
            mMap = googleMap
            //Request location updates:
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            val locationButton = (mMapView.findViewById<View>("1".toInt())
                .getParent() as View).findViewById<View>("2".toInt())


            // and next place it, for example, on bottom right (as Google Maps app)

            var rlp  = locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)

            googleMap.uiSettings.isRotateGesturesEnabled = true
            googleMap.setOnMarkerClickListener { marker: Marker ->
                this.onClickMarker(marker)
            }
        }

    }

    private fun onClickMarker( marker: Marker): Boolean {
        return if (marker.tag != null) {
            val intent = Intent(this, DetailActivity::class.java)
            val tag = marker.tag.toString()
            intent.putExtra("PlaceDetailResult", tag)
            startActivity(intent)
            this.finish()
            true
        } else {
            Log.e("Error", "onClickMarker: ERROR NO TAG")
            false
        }
    }


    override fun onLocationChanged(location: Location) {
        val mLatitude = location.latitude
        val mLongitude = location.longitude

        if (map != null) {
            val googleLocation = LatLng(mLatitude, mLongitude)
            map.moveCamera(CameraUpdateFactory.newLatLng(googleLocation))
            mPosition = "$mLatitude,$mLongitude"
            Log.d("TestLatLng", mPosition)
        }
    }
}