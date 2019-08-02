package com.mobilecomputing.quickshopper

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database.DatabaseUpdater
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.ResultsActivity
import java.util.jar.Manifest

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(permissions,0)

        val btnRestaurant :ImageButton = findViewById(R.id.btn_restaurant)
        val btnSupermarket : ImageButton = findViewById(R.id.btn_supermarket)
        val btnPharmacy : ImageButton = findViewById(R.id.btn_pharmacy)
        val btnTextile : ImageButton = findViewById(R.id.btn_textile)
        val gpsModule : GPSModule = GPSModule(applicationContext)
        val databaseUpdater : DatabaseUpdater = DatabaseUpdater(applicationContext)


        btnRestaurant.setOnClickListener{
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("category", "Restaurants")
            startActivity(intent)
        }

        btnSupermarket.setOnClickListener {
            var location:Location? = gpsModule.getLocation()
            if(location!=null){
                Log.d("lasa","what")
                Toast.makeText(this,"latitude: ${location.latitude} \n longtitude: ${location.longitude}", android.widget.Toast.LENGTH_LONG).show()
            }
            else{
                Log.d("lasa","what")
                Toast.makeText(this,"location is null", Toast.LENGTH_SHORT).show()
            }

        }

        btnPharmacy.setOnClickListener {
            databaseUpdater.saveShop("Restaurant", "Neluma Restaurant", 0, gpsModule.getLocation()!!)
        }



    }



}
