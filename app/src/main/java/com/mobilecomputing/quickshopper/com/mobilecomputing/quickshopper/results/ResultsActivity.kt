package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.location.Location.distanceBetween
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobilecomputing.quickshopper.R
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import kotlinx.android.synthetic.main.result_row.*
import kotlin.math.roundToInt

class ResultsActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val bundle:Bundle? = intent.extras
        val category = bundle!!.getString("category")
        title = category

        var listView:ListView = findViewById(R.id.results)

        var resultsList = mutableListOf<Result>()

/*        resultsList.add(Result("Neluma Restaurant", "Low", 1.4))
//        resultsList.add(Result("Liyanage Hotel", "Low", 1.4))
//        resultsList.add(Result("Kamatha Avanhala", "Medium", 1.4))
//        resultsList.add(Result("San Villa Restaurant", "High", 1.4))
//
//        listView.adapter = ResultAdapter(this, R.layout.result_row, resultsList)
*/

        val gpsModule = GPSModule(applicationContext)
        val myLocation : Location? = gpsModule.getLocation()

        val startAtLat = myLocation!!.latitude-0.01
        val endAtLat = myLocation.latitude+0.01
        val startAtLon = myLocation.longitude-0.01
        val endAtLon = myLocation.longitude+0.01

        //Log.d("lasa", location!!.latitude as String)



        val db = FirebaseDatabase.getInstance()
        val ref = db.reference
        Log.d("lasa",ref.child("Restaurant").toString())
        val query = ref.child(category)
                .orderByChild("location/latitude").startAt(startAtLat).endAt(endAtLat)

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (s in p0.children) {
                        if (startAtLon< (s.child("location/longitude").value as Double)
                                && (s.child("location/longitude").value as Double) < endAtLon) {

                            val shopName = s.child("shopName").value as String
                            val crowdLevel = when((s.child("crowdLevel").value as Long)){
                                                            0L -> 0
                                                            1L -> 1
                                                            else -> 2}
                            val address = s.child("address").value as String
                            val destLocation = Location("database")
                            destLocation.latitude = s.child("location/latitude").value as Double
                            destLocation.longitude = s.child("location/longitude").value as Double
                            var distance = (myLocation.distanceTo(destLocation)/100).roundToInt().toFloat()/10
//                            var dist :FloatArray = FloatArray(1)
//                            distanceBetween(myLocation.latitude,myLocation.longitude,
//                                    destLocation.latitude,destLocation.longitude,dist)

                            val result = Result(shopName, crowdLevel, distance, address)

                            resultsList.add(result)
                        }
                    }
                }
                listView.adapter = ResultAdapter(applicationContext, R.layout.result_row, resultsList.sortedBy {it.crowdLevel})
            }

            override fun onCancelled(p0: DatabaseError) {}
        })

//        var sortedList = mutableListOf<Result>()
//        resultsList = sortByCrowd(resultsList) as MutableList<Result>
        listView.adapter = ResultAdapter(applicationContext, R.layout.result_row, resultsList.sortedBy {it.crowdLevel})



        listView.setOnItemClickListener{ parent, view, position, id ->
            val uri = "geo:0,0?q="+resultsList.sortedBy {it.crowdLevel}[position].address
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            applicationContext.startActivity(intent)
        }
    }

//    fun sortByCrowd (resultList:List<Result>): List<Result>{
//        return resultList.sortedBy {it.crowdLevel}
//    }
//
//    fun sortByDistance (resultList: List<Result>): List<Result>{
//        return resultList.sortedBy {it.distance}
//    }
}