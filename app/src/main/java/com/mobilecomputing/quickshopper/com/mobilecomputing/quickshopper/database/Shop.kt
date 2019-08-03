package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database

import android.location.Address
import android.location.Location

data class Shop (val shopName:String, val crowdLevel:Int, val location:Location, val address: String)