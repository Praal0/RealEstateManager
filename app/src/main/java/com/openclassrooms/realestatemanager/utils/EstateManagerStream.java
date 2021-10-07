package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.geocoding_api.GeocodingApi;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EstateManagerStream {
    /**
     * Create stream for Geocoding
     * @param address
     * @return
     */
    public static Observable<GeocodingApi> streamFetchGeocode (String address) {
        EstateManagerService estateManagerService = EstateManagerRetrofitObject.retrofit.create(EstateManagerService.class);
        return estateManagerService.getGeocode(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
