package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import com.mobilecomputing.quickshopper.R

class ResultsActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val bundle:Bundle? = intent.extras
        title = bundle!!.getString("category")

        var listView:ListView = findViewById(R.id.results)

        var resultsList = mutableListOf<Result>()

        resultsList.add(Result("Neluma Restaurant", "Low", 1.4))
        resultsList.add(Result("Liyanage Hotel", "Low", 1.4))
        resultsList.add(Result("Kamatha Avanhala", "Medium", 1.4))
        resultsList.add(Result("San Villa Restaurant", "High", 1.4))

        listView.adapter = ResultAdapter(this, R.layout.result_row, resultsList)

    }
}