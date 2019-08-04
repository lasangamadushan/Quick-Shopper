package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper

import android.location.Location
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.mobilecomputing.quickshopper.R
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.Result
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.ResultAdapter
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

open class MJobExecutor : AsyncTask<Void, Void, String>() {
    private val url = URL("http://httpbin.org/post")
    var type = ""
    var id = ""
    var myLocation = Location("none")

    init {
        Log.d("lasa", "initialized")
    }

    override fun doInBackground(vararg params: Void?): String {
        Log.d("lasa", "doing")
        if (getLocationInfo()){
            sendData(getJsonObject(id, type, getNoiseValue(), getMovementData()))
        }
        return "ok this is working"
    }


    fun setLoc(loc: Location) {
        myLocation = loc
    }

    fun getJsonObject(shopId: String, shopType: String, noiseValue: Float, moveData: Float): JSONObject {
        var jsonObject = JSONObject()
        jsonObject.put("shop ID", shopId)
        jsonObject.put("shop type", shopType)
        jsonObject.put("noise value", noiseValue)
        jsonObject.put("movement data", moveData)
        return jsonObject
    }

    fun sendData(data: JSONObject) {
        with(url.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(outputStream)
            wr.write(data.toString())
            wr.flush()

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")
            }
        }
    }

    fun getLocationInfo(): Boolean {
        var res = false

        val db = FirebaseDatabase.getInstance()
        val ref = db.reference

        var tolerance = 0.01
        var categories = arrayOf("Restaurants", "Supermarkets", "Pharmacies", "Textiles")

        var done = false
        for (category in categories){
            tolerance = when(category){
                "Restaurants" -> 0.00005 //10mx10m
                "Supermarkets" -> 0.0001 //20mx20m
                "Pharmacies" -> 0.00001  //2mx2m
                "Textiles" -> 0.0001     //20mx20m
                else -> 0.0
            }

            val startAtLat = myLocation.latitude-tolerance
            val endAtLat = myLocation.latitude+tolerance
            val startAtLon = myLocation.longitude-tolerance
            val endAtLon = myLocation.longitude+tolerance

            Log.d("lasa", "my location ${myLocation.latitude}")

            val query = ref.child(category)
                    .orderByChild("location/latitude").startAt(startAtLat).endAt(endAtLat)


            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (s in p0.children) {
                            Log.d("lasa", "s")
                            if (startAtLon< (s.child("location/longitude").value as Double)
                                    && (s.child("location/longitude").value as Double) < endAtLon) {
                                Log.d("lasa", "result is there")
                                type = category
                                Log.d("lasa", category)
                                id = s.key.toString()
                                Log.d("lasa", id)
                                res = true
                            }
                        }
                    }

                    done=true
                }

                override fun onCancelled(p0: DatabaseError) {}
            })

        }
        while (!done){}
        Thread.sleep(1_000)
        Log.d("lasa", res.toString())
        return res
    }

    fun getNoiseValue():Float{
        var audioDataCollector = AudioDataCollector()
        var avg = audioDataCollector.getData()
        Log.d("lasa", avg.toString())
        return avg
    }

    fun getMovementData():Float{
        return 1.0f
    }
}