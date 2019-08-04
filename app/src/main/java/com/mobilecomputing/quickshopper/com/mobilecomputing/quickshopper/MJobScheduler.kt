package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.location.GPSModule

class MJobScheduler : JobService() {

    private lateinit var jobExecutor: MJobExecutor

    override fun onCreate() {
        super.onCreate()
        Log.d("lasa", "created")
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Toast.makeText(applicationContext, "stop", Toast.LENGTH_LONG).show()
        jobExecutor.cancel(true)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("lasa", "wtf")
        Toast.makeText(applicationContext, "started", Toast.LENGTH_LONG).show()
         @SuppressLint("StaticFieldLeak")
         jobExecutor = object : MJobExecutor(){

             override fun onPreExecute() {
                 super.onPreExecute()
                 val gpsModule = GPSModule(applicationContext)
                 val myLocation : Location? = gpsModule.getLocation()
                 setLoc(myLocation!!)
             }

             override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Toast.makeText(applicationContext, "hello", Toast.LENGTH_LONG).show()
                jobFinished(params, false)

            }

        }

        jobExecutor.execute()
        return true
    }
}