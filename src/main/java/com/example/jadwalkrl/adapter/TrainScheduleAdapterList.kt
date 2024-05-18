package com.example.jadwalkrl.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.jadwalkrl.R
import com.example.jadwalkrl.models.ScheduleTrain

class TrainScheduleAdapterList(context: Context, private val trainPosition: List<ScheduleTrain>) :
    ArrayAdapter<ScheduleTrain>(context, 0, trainPosition) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_line_station, parent, false)

        val train = getItem(position)

        val timeEstimation: TextView = itemView.findViewById(R.id.timeEstimation)
        val stationName: TextView = itemView.findViewById(R.id.stationName)
        val isTransit: TextView = itemView.findViewById(R.id.isTransit)
        val bgCircleTransit: View = itemView.findViewById(R.id.bgCircleTransit)

        train?.let {
            timeEstimation.text = it.timeEstimation
            stationName.text = it.stationName

            val transitRecyclerView = itemView.findViewById<RecyclerView>(R.id.transitRecyclerView)
            val isTransitStation = it.transit is List<*>

            transitRecyclerView.visibility = if (isTransitStation) View.VISIBLE else View.GONE
            isTransit.visibility = if (isTransitStation) View.VISIBLE else View.GONE
            isTransit.setBackgroundColor(
                if (isTransitStation) Color.parseColor("#ff0000")
                else Color.parseColor("#ff0099cc")
            )



            if (isTransitStation) {
                transitRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                transitRecyclerView.adapter = TransitListViewAdapter(context, it.transit as List<String>)
            }
        }

        return itemView
    }
}
