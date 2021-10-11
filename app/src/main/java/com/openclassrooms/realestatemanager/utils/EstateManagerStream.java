package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.models.geocodingAPI.Geocoding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EstateManagerStream {
    private static String GOOGLE_MAP_API_KEY = BuildConfig.API_KEY;

    /**
     * Create stream for Geocoding
     * @param address
     * @return
     */
    public static Observable<Geocoding> streamFetchGeocode (String address) {
        EstateManagerService estateManagerService = EstateManagerRetrofitObject.retrofit.create(EstateManagerService.class);
        return estateManagerService.getGeocode(address,GOOGLE_MAP_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
