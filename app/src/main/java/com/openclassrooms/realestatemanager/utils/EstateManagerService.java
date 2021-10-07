package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.geocoding_api.GeocodingApi;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EstateManagerService {
    String GOOGLE_MAP_API_KEY = "AIzaSyAJM3OAnxshyxdLpqktVicVUUM_D74yKCU";

    /**
     * Create end point
     * @param address
     * @return
     */
    //Geocoding API Request
    @GET("geocode/json?key="+GOOGLE_MAP_API_KEY)
    Observable<GeocodingApi> getGeocode (@Query("address") String address);
}
