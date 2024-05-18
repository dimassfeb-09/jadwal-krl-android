package com.example.jadwalkrl

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.jadwalkrl.adapter.StationAdapterList
import com.example.jadwalkrl.models.Station
import com.example.jadwalkrl.models.StationResponse
import com.example.jadwalkrl.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var stations: List<Station>
    private lateinit var editTextSearch: EditText
    private lateinit var listViewStation: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        setupSearchListener()
        fetchDataStationFromApi()
    }

    private fun initializeViews() {
        editTextSearch = findViewById(R.id.editTextStationDestination)
        listViewStation = findViewById(R.id.stationList)
    }

    private fun setupSearchListener() {
        editTextSearch.addTextChangedListener { text ->
            filterStations(text.toString().trim())
        }
    }

    private fun filterStations(searchText: String) {
        val filteredStations = stations.filter { station ->
            station.stationName.contains(searchText, ignoreCase = true)
        }

        val selectedStation = filteredStations.ifEmpty {
            listOf(Station("0", "Tidak ditemukan", 0, 0))
        }

        val adapter = StationAdapterList(this, selectedStation)
        listViewStation.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun fetchDataStationFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-partner.krl.co.id/krlweb/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val call = apiService.fetchDataStation()
        call.enqueue(object : Callback<StationResponse> {
            override fun onResponse(call: Call<StationResponse>, response: Response<StationResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { stationResponse ->
                        stations = stationResponse.data
                        updateListView()
                    }
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<StationResponse>, throwable: Throwable) {
                Log.e("API", "Failed to fetch data: ${throwable.message}")
            }
        })
    }

    private fun updateListView() {
        val adapter = StationAdapterList(this@MainActivity, stations)
        listViewStation.adapter = adapter
    }
}
