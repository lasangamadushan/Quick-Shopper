package com.mobilecomputing.quickshopper

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database.DatabaseUpdater
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.ResultsActivity
import java.util.jar.Manifest
import android.support.v4.content.ContextCompat.startActivity
import java.util.*


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
            databaseUpdater.saveShop("Restaurant", "Neluma Restaurant", 0, gpsModule.getLocation()!!, "Neluma+Restaurant,+B295,+Piliyandala")
        }

        btnTextile.setOnClickListener {
            //val uri = String.format(Locale.ENGLISH, "geo:%f,%f", 6.801518, 79.922094)
//            val uri = "geo:0,0?q="+"Neluma+Restaurant,+B295,+Piliyandala"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//            startActivity(intent)

            /*val geocoder = Geocoder(this, Locale.getDefault())
            var addresses: List<Address> = emptyList()
            addresses = geocoder.getFromLocation(
                    6.816234,
                    79.936292,
                    // In this sample, we get just a single address.
                    1)
            val place = addresses.get(0).locality
            Toast.makeText(this,addresses.toString(), Toast.LENGTH_SHORT).show()
            */
        }



    }



}
