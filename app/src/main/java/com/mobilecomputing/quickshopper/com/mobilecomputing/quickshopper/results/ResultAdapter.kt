package com.mobilecomputing.quickshopper.com.mobilecomputing.quickshopper.results

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.mobilecomputing.quickshopper.R
import kotlinx.android.synthetic.main.result_row.view.*

class ResultAdapter (var ctx:Context, var resource:Int, var results:List<Result>)
    : ArrayAdapter<Result>(ctx,resource,results){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater:LayoutInflater = LayoutInflater.from(ctx)

        val view:View = layoutInflater.inflate(resource, null)


        val layout:LinearLayout = view.findViewById(R.id.row)
        val shopName:TextView = view.findViewById(R.id.shop_name)
        val crowdLevel:TextView = view.findViewById(R.id.crowd_level)
        val distance:TextView = view.findViewById(R.id.distance)

        var result:Result = results[position]

        shopName.text = result.shopName
        crowdLevel.text = "Crowd : ${when(result.crowdLevel){
            0 -> "Low"
            1 -> "Medium"
            else -> "High"
        }}"
        distance.text = "Distance : ${result.distance}km"
        layout.setBackgroundResource(when(result.crowdLevel){
            0 -> R.drawable.row_low_crowd
            1 -> R.drawable.row_medium_crowd
            else -> R.drawable.row_high_crowd

        })

        return view
    }
}