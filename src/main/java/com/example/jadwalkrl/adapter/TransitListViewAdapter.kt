package com.example.jadwalkrl.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jadwalkrl.R


internal class TransitListViewAdapter(var context: Context, var data: List<String>) :
    RecyclerView.Adapter<TransitListViewAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.train_transit, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Log.d("colorrrsssss", "onBindViewHolder: ${data[position]}")
        holder.iconTrain.setColorFilter(Color.parseColor(data[position]))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iconTrain: ImageView
        init {
            iconTrain = itemView.findViewById<ImageView>(R.id.trainIconTransit)
        }
    }
}