package com.example.jadwalkrl

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.jadwalkrl.adapter.StationScheduleAdapterList
import com.example.jadwalkrl.models.ScheduleStation
import com.example.jadwalkrl.models.StationScheduleResponse
import com.example.jadwalkrl.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DetailRouteStationActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var listViewScheduleStation: ListView
    private lateinit var title: TextView
    private lateinit var dataNotFound: TextView
    private lateinit var loadingData: TextView
    private var stationId: String? = null
    private var stationName: String? = null
    private var timeFrom: String? = null
    private var timeEnd: String? = null
    private var stations: List<ScheduleStation> = emptyList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_route_station)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getExtraData()
        initializeViews()
        getCurrentTimeFormatted()
        fetchDataScheduleStationFromApi()
    }


    private fun initializeViews() {
        listViewScheduleStation = findViewById(R.id.stationList)
        title = findViewById(R.id.title)
        dataNotFound = findViewById(R.id.dataNotFound)
        loadingData = findViewById(R.id.loadingData)
        dataNotFound.visibility = View.INVISIBLE; loadingData.visibility = View.VISIBLE
        title.text = "Posisi Kereta $stationName"
    }


    private fun getExtraData() {
        stationId = intent.getStringExtra("stationId")
        stationName = intent.getStringExtra("stationName")?.capitalize()
        timeFrom = intent.getStringExtra("timeFrom")
        timeEnd = intent.getStringExtra("timeEnd")
    }

    private fun fetchDataScheduleStationFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-partner.krl.co.id/krlweb/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        val call = apiService.fetchDataScheduleStation(stationId, timeFrom, timeEnd)
        call.enqueue(object : Callback<StationScheduleResponse> {
            override fun onResponse(
                call: Call<StationScheduleResponse>,
                response: Response<StationScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val stationResponse = response.body()
                    Log.d("responseeee", "onResponse: $stationResponse")
                    stationResponse?.data?.let {
                        stations = it
                        updateListView()
                    }
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
                handleDataVisibility()
            }

            override fun onFailure(call: Call<StationScheduleResponse>, throwable: Throwable) {
                Log.e("API", "Failed to fetch data: ${throwable.message}")

                handleDataVisibility()
            }
        })
    }

    private fun handleDataVisibility() {
        loadingData.visibility = View.INVISIBLE
        if (stations.isEmpty()) {
            dataNotFound.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTimeFormatted() {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val currentTime = LocalTime.now()

        timeFrom = timeFrom ?: currentTime.format(formatter)
        timeEnd = timeEnd ?: currentTime.plusHours(1).format(formatter)
    }


    private fun updateListView() {
        val adapter = StationScheduleAdapterList(this@DetailRouteStationActivity, stations, stationName.toString())
        listViewScheduleStation.adapter = adapter
    }

}
