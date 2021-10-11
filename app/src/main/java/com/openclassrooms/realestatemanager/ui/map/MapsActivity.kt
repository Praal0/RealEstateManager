package com.openclassrooms.realestatemanager.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMapsBinding
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import com.openclassrooms.realestatemanager.viewModel.LocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MapsActivity : BaseActivity(), OnMapReadyCallback,LocationListener {

    protected val PERMS_CALL_ID = 200
    private lateinit var map: GoogleMap
    private lateinit var mPosition: String
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private val locationViewModel: LocationViewModel by viewModels()

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
        if (Objects.requireNonNull(locationManager)
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10f, this)
            Log.e("GPSProvider", "testGPS")
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER, 15000L, 10f, this
            )
            Log.e("PassiveProvider", "testPassive")
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 15000L, 10f, this
            )
            Log.e("NetWorkProvider", "testNetwork")
        }
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

        if (map != null) {
            val googleLocation = LatLng(mLatitude, mLongitude)
            map.moveCamera(CameraUpdateFactory.newLatLng(googleLocation))
            mPosition = "$mLatitude,$mLongitude"
            Log.d("TestLatLng", mPosition)
        }
    }

    /**
     * For update location
     */
    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(this)
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
    override fun onMapReady(googleMap: GoogleMap) {
        if (Utils.isInternetAvailable(Objects.requireNonNull(this))) {
            map = googleMap
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15f))
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
            googleMap.isMyLocationEnabled = true
        } else {
            Snackbar.make(findViewById(R.id.map), "No internet avalaible", Snackbar.LENGTH_SHORT)
                .show()
        }
    }


}


