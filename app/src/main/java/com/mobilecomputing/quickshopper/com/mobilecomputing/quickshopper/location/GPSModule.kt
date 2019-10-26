package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class GPSModule(ctx:Context) : LocationListener{
    val context:Context
    init{
        context = ctx
    }

    fun getLocation():Location?{
        val locationManager:LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGpsEnabled:Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled){
            try {
                 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10f,this)
                 var location:Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                return location
            }catch (ex:SecurityException){
                Log.d("lasanga", "no permision")
                return null
            }
        }
        else{
            return null
        }
    }

    override fun onLocationChanged(location: Location?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
         //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String?) {
        //To change body of created functions use File | Settings | File Templates.
    }

}