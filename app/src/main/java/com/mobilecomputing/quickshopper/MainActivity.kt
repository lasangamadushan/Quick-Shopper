package com.mobilecomputing.quickshopper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.ResultsActivity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRestaurant :ImageButton = findViewById(R.id.btn_restaurant)
        btnRestaurant.setOnClickListener{
            val intent = Intent(this, ResultsActivity::class.java)
            intent.putExtra("category", "Restaurants")
            startActivity(intent)
        }
    }



}
