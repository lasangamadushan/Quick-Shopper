package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database

import android.content.Context
import android.location.Location
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class DatabaseUpdater(ctx: Context) {
    val context:Context
    init{
        context = ctx
    }

    fun saveShop(type: String, shopeName: String, crowdLevel: Int, location: Location) {
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(type)
        val shopId = ref.push().key

        val shop = Shop(shopeName, crowdLevel, location)

        ref.child(shopId!!).setValue(shop).addOnCompleteListener{
            Toast.makeText(context, "shop saved successfully", Toast.LENGTH_LONG).show()
        }

    }
}