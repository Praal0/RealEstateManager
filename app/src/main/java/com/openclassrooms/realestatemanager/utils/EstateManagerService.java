package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstateManagerService {


    /**
     * Create end point
     * @param address
     * @return
     */
    //Geocoding API Request
    @GET("maps/api/geocode/json?")
    Observable<Geocoding> getGeocode (@Query("address") String address,@Query("key") String key);
}
