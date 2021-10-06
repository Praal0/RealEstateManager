package com.openclassrooms.realestatemanager.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;
import com.openclassrooms.realestatemanager.ui.baseActivity.BaseActivity;
import com.openclassrooms.realestatemanager.utils.EstateManagerStream;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, LocationListener, Serializable {

    private static final String TAG = MapActivity.class.getSimpleName();

    protected static final int PERMS_CALL_ID = 200;
    private GoogleMap map;
    private String mPosition;
    private LocationManager locationManager;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<String> adressList = new ArrayList<>();
    private List<Long> idList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //For map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    /**
     * For permissions to position access
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMS_CALL_ID);
            return;
        }
        locationManager = (LocationManager) (getSystemService(Context.LOCATION_SERVICE));

        if (Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 15000, 10, this);
            Log.e("GPSProvider", "testGPS");
        } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 15000, 10, this);
            Log.e("PassiveProvider", "testPassive");
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 15000, 10, this);
            Log.e("NetWorkProvider", "testNetwork");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL_ID) {
            checkPermissions();
        }
    }

    /**
     * For update location
     */
    @Override
    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }

    }

    /**
     * Retrieve User location
     *
     * @param location
     */
    public void onLocationChanged(Location location) {
        double mLatitude = location.getLatitude();
        double mLongitude = location.getLongitude();

        if (map != null) {
            LatLng googleLocation = new LatLng(mLatitude, mLongitude);
            map.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
            mPosition = mLatitude + "," + mLongitude;
            Log.d("TestLatLng", mPosition);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    /**
     * For map display
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (Utils.isInternetAvailable(this)) {
            map = googleMap;

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMS_CALL_ID);
                return;
            }

            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.mapstyle));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }

            }catch (Resources.NotFoundException e){
                Log.e(TAG, "Can't find style. Error: ", e);
            }

            googleMap.setMyLocationEnabled(true);

        } else {
            Snackbar.make(findViewById(R.id.map), "No internet avalaible", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Dispose subscription
     */
    private void disposeWhenDestroy() {
        mCompositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }


}
