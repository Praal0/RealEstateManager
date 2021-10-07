package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import com.openclassrooms.realestatemanager.viewModel.MapViewModel
import android.location.LocationManager

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import android.os.Build
import androidx.annotation.NonNull

import androidx.annotation.RequiresApi
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity


class MapsActivity : BaseActivity(), OnMapReadyCallback,LocationListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mPosition : String
    private var locationManager: LocationManager? = null
    private lateinit var binding: ActivityMapsBinding
    private val map: GoogleMap? = null
    private lateinit var mMapView: MapView
    private val mViewModel: MapViewModel by viewModels()
    private val perms = ("Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        refresh()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            checkPermissions()
        }
    }

    /**
     * For update location
     */
    override fun onPause() {
        super.onPause()
        if (locationManager != null) {
            locationManager!!.removeUpdates(this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        if (EasyPermissions.hasPermissions(this, perms)){
            mMap = googleMap
            //Request location updates:
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isCompassEnabled
            googleMap.setMyLocationEnabled(true)
            googleMap.uiSettings.isZoomControlsEnabled = true
            val locationButton = (mMapView.findViewById<View>("1".toInt())
                .getParent() as View).findViewById<View>("2".toInt())


            // and next place it, for example, on bottom right (as Google Maps app)
            val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
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



    /**
     * For permissions to position access
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
            return
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10F, this)
            Log.e("GPSProvider", "testGPS")
        } else if (locationManager!!.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager!!.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 15000, 10F, this)
            Log.e("PassiveProvider", "testPassive")
        } else if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 15000, 10F, this)
            Log.e("NetWorkProvider", "testNetwork")
        }
    }

    fun refresh(){
        // No GPS permission
        if (EasyPermissions.hasPermissions(this, perms)) {
            mViewModel.startLocationRequest(this)
            mViewModel.getLocation()?.observe(this) { location ->
                if (location != null) {
                    val latLng = LatLng(location.getLat(), location.getLng())
                    mViewModel.updateCurrentUserPosition(latLng)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
                }
            }
        } else {
            EasyPermissions.requestPermissions(
                this, "Need permission for use MapView and ListView",
                124, perms
            )
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