package com.example.jadwalkrl.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.jadwalkrl.DetailRouteStationActivity
import com.example.jadwalkrl.R
import com.example.jadwalkrl.models.Station

class StationAdapterList(context: Context, private val listStation: List<Station>) :
    ArrayAdapter<Station>(context, 0, listStation) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_station, parent, false)
        val station = getItem(position)

        val imageView: ImageView = itemView.findViewById(R.id.trainIcon)
        val stationNameTextView: TextView = itemView.findViewById(R.id.titleStation)

        station?.let {
            val station = it

            val bgColor = if (station.fgEnable == 1) android.R.color.white else R.color.gray_light
            itemView.setBackgroundColor(ContextCompat.getColor(context, bgColor))

            imageView.isVisible = station.fgEnable == 1
            itemView.isClickable = station.fgEnable == 1
            stationNameTextView.text = station.stationName

            if (station.fgEnable == 1) {
                itemView.setOnClickListener {
                    val intent = Intent(context, DetailRouteStationActivity::class.java).apply {
                        putExtra("stationId", station.stationId)
                        putExtra("stationName", station.stationName)
                    }
                    context.startActivity(intent)
                }
            } else {
                itemView.setOnClickListener(null)
            }
        }

        return itemView
    }

}
