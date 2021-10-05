package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstateManagerService {
    String GOOGLE_MAP_API_KEY = BuildConfig.API_KEY;

    /**
     * Create end point
     * @param address
     * @return
     */
    //Geocoding API Request
    @GET("maps/api/geocode/json?key="+GOOGLE_MAP_API_KEY)
    Observable<Geocoding> getGeocode (@Query("address") String address);
}
