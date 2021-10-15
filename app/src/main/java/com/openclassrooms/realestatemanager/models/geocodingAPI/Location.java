
package com.openclassrooms.realestatemanager.models.geocodingAPI;

import com.google.gson.annotations.SerializedName;


public class Location {

    public Location(Double lat,Double lng){
        this.mLat = lat;
        this.mLng = lng;
    }

    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lng")
    private Double mLng;

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
    }

}
