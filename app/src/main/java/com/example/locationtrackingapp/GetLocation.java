package com.example.locationtrackingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class GetLocation {
    Context context;

    public GetLocation(Context c) {
        this.context = c;
    }

    public Location getCurrentLocation () {
        LocationManager locationManager;
        String context1 = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Location TODO = null;
            return TODO;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        return location;
    }
    public String updateWithNewLocation(Location location) {
        String latLongString;

        if (location != null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            latLongString = "Lat:" + lat + "\nLong:" + lng;
        }else{
            latLongString = "No Location";
        }

        return latLongString;
    }
    public String updateLatitude(Location location){
        String Latitude;
        if(location != null){
            double lat = location.getLatitude();
            Latitude = ""+lat;
        }
        else{
            Latitude = "NO Latitude";
        }
        return Latitude;
    }
    public String updateLongitude(Location location){
        String Longitude;
        if(location != null){
            double longi = location.getLongitude();
            Longitude = ""+longi;
        }
        else{
            Longitude= "NO Latitude";
        }
        return Longitude;
    }

}
