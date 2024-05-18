package com.example.jadwalkrl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.jadwalkrl.adapter.TrainScheduleAdapterList
import com.example.jadwalkrl.models.ScheduleTrain
import com.example.jadwalkrl.models.TrainScheduleResponse
import com.example.jadwalkrl.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailScheduleTrainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var listScheduleTrain: ListView
    private var trainId: String? = null
    private var trains: List<ScheduleTrain> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_schedule_train)

        initializeViews()
        getExtraData()
        fetchDataScheduleStationFromApi()
    }

    private fun initializeViews() {
        listScheduleTrain = findViewById(R.id.listScheduleTrain)
    }

    private fun getExtraData() {
        trainId = intent.getStringExtra("trainId")
        Log.d("trainIdssss", "onCreate: " + trainId)
    }

    private fun fetchDataScheduleStationFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-partner.krl.co.id/krlweb/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        val call = apiService.fetchDataScheduleTrain(trainId)
        call.enqueue(object : Callback<TrainScheduleResponse> {
            override fun onResponse(
                call: Call<TrainScheduleResponse>,
                response: Response<TrainScheduleResponse>
            ) {
                if (response.isSuccessful) {
                    val stationResponse = response.body()
                    Log.d("responseeee", "onResponse: $stationResponse")
                    stationResponse?.data?.let {
                        trains = it
                        updateListView()
                    }
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TrainScheduleResponse>, throwable: Throwable) {
                Log.e("API", "Failed to fetch data: ${throwable.message}")

            }
        })
    }

    private fun updateListView() {
        val adapter = TrainScheduleAdapterList(this@DetailScheduleTrainActivity, trains)
        listScheduleTrain.adapter = adapter
    }


}