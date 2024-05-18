package com.example.jadwalkrl.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.jadwalkrl.R
import com.example.jadwalkrl.models.ScheduleStation
import com.example.jadwalkrl.DetailScheduleTrainActivity
import com.example.jadwalkrl.models.StationResponse
import com.example.jadwalkrl.models.TariffTrain
import com.example.jadwalkrl.models.TrainTariffResponse
import com.example.jadwalkrl.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StationScheduleAdapterList(context: Context, private val listStation: List<ScheduleStation>, private val cityFrom: String) :
    ArrayAdapter<ScheduleStation>(context, 0, listStation) {

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-partner.krl.co.id/krlweb/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view_detail_route, parent, false)
        val station = getItem(position)

        val routeName: TextView = itemView.findViewById(R.id.routeName)
        val timeEstimation: TextView = itemView.findViewById(R.id.timeEstimation)
        val timeDestination: TextView = itemView.findViewById(R.id.timeDestination)
        val fromDestination: TextView = itemView.findViewById(R.id.fromDestination)
        val toDestination: TextView = itemView.findViewById(R.id.toDestination)
        val hoursArrived: TextView = itemView.findViewById(R.id.hoursArrived)
        val trainId: TextView = itemView.findViewById(R.id.trainId)
        val tarif: TextView = itemView.findViewById(R.id.tarif)

        station?.let {

            routeName.setBackgroundColor(Color.parseColor(it.color))
            val roundedBackground = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 10f
                setColor(Color.parseColor(it.color))
            }
            routeName.background = roundedBackground
            routeName.text = it.routeName

            timeEstimation.text = "${it.timeEstimation} WIB"
            timeDestination.text = "${it.destinationTime} WIB"
            hoursArrived.text = calculateTimeDifference(it.timeEstimation, it.destinationTime)
            fromDestination.text = cityFrom
            toDestination.text = it.destination
            trainId.text = it.trainId

            fetchDataStation(cityFrom, it.destination) { tariffTrain ->
                checkTarif(tariffTrain) {tariff ->
                    tarif.text= "Rp ${tariff?.fare}"
                }
            }

        }

        itemView.setOnClickListener {
            station?.let {
                val intent = Intent(context, DetailScheduleTrainActivity::class.java)
                intent.putExtra("trainId", it.trainId)
                context.startActivity(intent)
            }
        }

        return itemView
    }


    private fun checkTarif(tariffTrain: TariffTrain?, callback: (TariffTrain?) -> Unit) {
        apiService.fetchDataTarifByStationCode(tariffTrain?.staCodeFrom, tariffTrain?.staCodeTo).enqueue(object : Callback<TrainTariffResponse> {
            override fun onResponse(call: Call<TrainTariffResponse>, response: Response<TrainTariffResponse>) {

                tariffTrain?.fare = response.body()?.data?.first()?.fare
                callback(tariffTrain)
            }

            override fun onFailure(call: Call<TrainTariffResponse>, throwable: Throwable) {
                callback(null)
            }
        })
    }

    fun calculateTimeDifference(timeEst: String, destTime: String): String {
        val timeEstParts = timeEst.split(":").map { it.toInt() }
        val destTimeParts = destTime.split(":").map { it.toInt() }

        val totalSecondsTime1 = timeEstParts[0] * 3600 + timeEstParts[1] * 60 + timeEstParts[2]
        val totalSecondsTime2 = destTimeParts[0] * 3600 + destTimeParts[1] * 60 + destTimeParts[2]

        val differenceSeconds = totalSecondsTime2 - totalSecondsTime1

        val hours = differenceSeconds / 3600
        val remainingSeconds = differenceSeconds % 3600

        val minutes = remainingSeconds / 60

        val formattedDifference = "${hours}jam ${minutes}menit"

        return formattedDifference
    }



    private fun fetchDataStation(stationNameFrom: String, stationNameEnd: String, callback: (TariffTrain?) -> Unit) {
        apiService.fetchDataStation().enqueue(object : Callback<StationResponse> {
            override fun onResponse(call: Call<StationResponse>, response: Response<StationResponse>) {
                val stationCodeFrom = getStationCode(response, stationNameFrom)
                val stationCodeEnd = getStationCode(response, stationNameEnd)

                val stationDetail = TariffTrain(
                    stationCodeFrom,
                    stationNameFrom,
                    stationCodeEnd,
                    stationNameEnd,
                    null,
                    null
                )
                callback(stationDetail)
            }

            override fun onFailure(call: Call<StationResponse>, throwable: Throwable) {
                callback(null) // Handle failure by returning null
            }
        })
    }


    private fun getStationCode(response: Response<StationResponse>, stationName: String): String? {
        return response.body()?.data?.find { it.stationName == stationName }?.stationId
    }

    private fun drawableLineArea(stationColor: String): GradientDrawable {
        val lineAreaDrawable = GradientDrawable()
        lineAreaDrawable.shape = GradientDrawable.RECTANGLE
        lineAreaDrawable.cornerRadius = 20f
        lineAreaDrawable.setColor(Color.parseColor(stationColor))
        return lineAreaDrawable
    }
}
