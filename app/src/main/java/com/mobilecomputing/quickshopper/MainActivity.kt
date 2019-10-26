package com.mobilecomputing.quickshopper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.AudioDataCollector
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.MJobExecutor
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.MJobScheduler
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.database.DatabaseUpdater
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results.ResultsActivity


class MainActivity : Activity() {


    companion object {
        val JOB_ID = 101
    }
    private lateinit var jobScheduler: JobScheduler
    private lateinit var jobInfo: JobInfo



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED)
        requestPermissions(permissions,0)

        val btnRestaurant :ImageButton = findViewById(R.id.btn_restaurant)
        val btnSupermarket : ImageButton = findViewById(R.id.btn_supermarket)
        val btnPharmacy : ImageButton = findViewById(R.id.btn_pharmacy)
        val btnTextile : ImageButton = findViewById(R.id.btn_textile)
        val gpsModule : GPSModule = GPSModule(applicationContext)
        val databaseUpdater : DatabaseUpdater = DatabaseUpdater(applicationContext)

        setupJobSchedule()
        //jobScheduler.schedule(jobInfo)
        //jobScheduler.cancelAll()

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
//            val startServiceIntent = Intent(this, MJobScheduler::class.java)
//            startService(startServiceIntent)

            databaseUpdater.saveShop("Restaurants", "Neluma Restaurant", 0, gpsModule.getLocation()!!, "Neluma+Restaurant,+B295,+Piliyandala")
        }

        btnTextile.setOnClickListener {
            lateinit var task : MJobExecutor
            task = @SuppressLint("StaticFieldLeak")
            object :MJobExecutor(){
                override fun onPostExecute(result: String?) {
                    super.onPostExecute(result)
                    Toast.makeText(applicationContext, "hello", Toast.LENGTH_LONG).show()
                    }}
            task.setLoc(gpsModule.getLocation()!!)
            task.execute()


            //Toast.makeText(this,jobScheduler.getPendingJob(101).toString(), Toast.LENGTH_SHORT).show()
            //jobScheduler.cancel(JOB_ID)
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

    @SuppressLint("ServiceCast")
    fun setupJobSchedule(){
        print("hello")
        var cn = ComponentName(this, MJobScheduler::class.java)
        var builder:JobInfo.Builder = JobInfo.Builder(JOB_ID, cn)
        builder.setPeriodic(5000)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        //builder.setPersisted(true)
        jobInfo = builder.build()
        jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    }
}
