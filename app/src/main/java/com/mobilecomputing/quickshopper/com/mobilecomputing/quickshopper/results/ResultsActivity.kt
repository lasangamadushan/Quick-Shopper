package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobilecomputing.quickshopper.R
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database.Shop
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import kotlinx.android.synthetic.main.result_row.*

class ResultsActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val bundle:Bundle? = intent.extras
        title = bundle!!.getString("category")

        var listView:ListView = findViewById(R.id.results)

        var resultsList = mutableListOf<Result>()

/*        resultsList.add(Result("Neluma Restaurant", "Low", 1.4))
//        resultsList.add(Result("Liyanage Hotel", "Low", 1.4))
//        resultsList.add(Result("Kamatha Avanhala", "Medium", 1.4))
//        resultsList.add(Result("San Villa Restaurant", "High", 1.4))
//
//        listView.adapter = ResultAdapter(this, R.layout.result_row, resultsList)
*/

        val gpsModule : GPSModule = GPSModule(applicationContext)
        val location : Location? = gpsModule.getLocation()



        val db = FirebaseDatabase.getInstance()
        val ref = db.reference
        Log.d("lasa",ref.child("Restaurant").toString())
        val query = ref.child("Restaurant")
                .orderByChild("location/latitude").startAt(6.81).endAt(6.83)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (s in p0.children) {

                        if ((location!!.longitude-0.01)<(s.child("location/longitude").value as Double)
                                && (s.child("location/longitude").value as Double)< (location!!.longitude+0.01)) {

                            val shopName = s.child("shopName").value as String
                            val crowdLevel = when((s.child("crowdLevel").value as Long)){
                                                            0L -> "Low"
                                                            1L -> "Medium"
                                                            else -> "High"}

                            val result = Result(shopName, crowdLevel,1.5)

                            resultsList.add(result)
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        listView.adapter = ResultAdapter(this, R.layout.result_row, resultsList)
    }
}