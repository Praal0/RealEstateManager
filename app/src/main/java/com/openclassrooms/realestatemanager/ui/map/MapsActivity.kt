package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.ui.detail.DetailActivity
import com.openclassrooms.realestatemanager.utils.EstateManagerStream
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver


@AndroidEntryPoint
class MapsActivity : BaseActivity(), OnMapReadyCallback,LocationListener,OnMarkerClickListener {

    protected val PERMS_CALL_ID = 200
    private var map: GoogleMap?  = null
    private lateinit var toolbar : Toolbar
    private lateinit var mPosition: String

    private lateinit var binding: ActivityMapsBinding
    private  var locationManager: LocationManager? = null
    private lateinit var latLng : LatLng
    private val estateViewModel:EstateViewModel by viewModels()
    private val mapViewModel : MapViewModel by viewModels()
    private val mCompositeDisposable = CompositeDisposable()
    private var marker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Set title toolbar
        setToolbar()

        //Add marker
        addMarker()
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
                ), PERMS_CALL_ID
            )
            return
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10f, this)
            Log.e("GPSProvider", "testGPS")
        } else if (locationManager!!.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager!!.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER, 15000L, 10f, this
            )
            Log.e("PassiveProvider", "testPassive")
        } else if (locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 15000L, 10f, this
            )
            Log.e("NetWorkProvider", "testNetwork")
        }
    }

    private fun setToolbar() {
        toolbar = binding.includedToolbarAdd.simpleToolbar
        setSupportActionBar(toolbar)
        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab?.setTitle(R.string.title_activity_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
        }
    }

    override fun onLocationChanged(location: Location) {
        val mLatitude = location.latitude
        val mLongitude = location.longitude
        val googleLocation = LatLng(mLatitude, mLongitude)
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(googleLocation, 16f))
        mPosition = "$mLatitude,$mLongitude"
        Log.d("TestLatLng", mPosition)
    }

    /**
     * For update location
     */
    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (Utils.isInternetAvailable(Objects.requireNonNull(this))) {
            map = googleMap
            googleMap.uiSettings.isZoomControlsEnabled = true
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), PERMS_CALL_ID)
                return
            }
            //Request location updates:
            map!!.isMyLocationEnabled = true
            map!!.uiSettings.isCompassEnabled = true
            map!!.uiSettings.isMyLocationButtonEnabled = true
            mapViewModel.startLocationRequest(this)
            mapViewModel.getLocation()?.observe(this) { location ->
                if (location != null) {
                    latLng = LatLng(location.lat, location.lng)
                    mapViewModel.updateCurrentUserPosition(latLng)
                    map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                    googleMap.uiSettings.isMyLocationButtonEnabled = true
                    mapViewModel.updateCurrentUserPosition(latLng)
                }
            }
            map!!.uiSettings.isRotateGesturesEnabled = true
            googleMap.setOnMarkerClickListener(this);
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle))
                if (!success) {
                    Log.e(TAG, "Style parsing failed.")
                }
            }
            catch (e: NotFoundException) { Log.e(TAG, "Can't find style. Error: ", e) }
            googleMap.isMyLocationEnabled = true

        } else {
            Snackbar.make(findViewById(R.id.map), "No internet avalaible", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun addMarker() {
        estateViewModel.getEstates().observe(this, androidx.lifecycle.Observer {
            for (locationList in it){
                if (locationList.locationEstate.latitude == 0.0 && locationList.locationEstate.longitude == 0.0){
                    val completeAddress = locationList.locationEstate.address + locationList.locationEstate.city+ locationList.locationEstate.zipCode
                    EstateManagerStream.streamFetchGeocode(completeAddress)
                        .subscribeWith(object : DisposableObserver<Geocoding?>() {
                            override fun onNext(geocoding: Geocoding) {
                                if (!geocoding.results.isNullOrEmpty()){
                                    updateEstate(locationList,geocoding)
                                }else{
                                    Log.d("Geocoding","Geocoding : Null or Empty")
                                }
                            }
                            override fun onError(@NonNull e: Throwable) { Log.e("Geocoding","Error insert",e) }
                            override fun onComplete() {}
                        })
                }else{
                    latLng = LatLng(locationList.locationEstate.latitude, locationList.locationEstate.longitude)
                    if (locationList.sold){
                        marker = map?.addMarker(
                            MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        )
                    }else{
                        marker = map?.addMarker(
                            MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        )
                    }
                    marker?.tag = locationList.numMandat
                    marker?.showInfoWindow()
                }
            }
        })
    }

    private fun updateEstate(locationList: Estate, geocoding: Geocoding) {
        locationList.locationEstate.latitude = geocoding.results[0].geometry.location.lat
        locationList.locationEstate.longitude = geocoding.results[0].geometry.location.lng
        estateViewModel.updateEstate(locationList)
        latLng = LatLng(locationList.locationEstate.latitude, locationList.locationEstate.longitude)

        if (locationList.sold){
            marker = map?.addMarker(
                MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
        }else{
            marker = map?.addMarker(
                MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }

        //marker?.tag = locationList.estateId
        marker?.showInfoWindow()
    }

    /**
     * Dispose subscription
     */
    private fun disposeWhenDestroy() {
        mCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeWhenDestroy()
    }

    override fun onMarkerClick(marker : Marker): Boolean {
        return if (marker.tag != null){
            Log.e(TAG, "onClickMarker: " + marker.tag)
            val estateId: Long = marker.tag.toString().toLong()
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra("estate", estateId)
            startActivity(intent)
            true

        }else{
            Log.e(TAG, "onClickMarker: ERROR NO TAG")
            false
        }
    }
}


