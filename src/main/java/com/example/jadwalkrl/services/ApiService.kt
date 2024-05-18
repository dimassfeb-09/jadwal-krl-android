package com.example.jadwalkrl.services
import com.example.jadwalkrl.models.StationResponse
import com.example.jadwalkrl.models.StationScheduleResponse
import com.example.jadwalkrl.models.TrainScheduleResponse
import com.example.jadwalkrl.models.TrainTariffResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("krl-station")
    fun fetchDataStation(): Call<StationResponse>

    @GET("schedule")
    fun fetchDataScheduleStation(
        @Query("stationid") stationId: String?,
        @Query("timefrom") timeFrom: String?,
        @Query("timeto") timeTo: String?,
    ): Call<StationScheduleResponse>

    @GET("schedule-train")
    fun fetchDataScheduleTrain(
        @Query("trainid") trainId: String?,
    ): Call<TrainScheduleResponse>

    @GET("fare")
    fun fetchDataTarifByStationCode(
        @Query("stationfrom") stationFrom: String?,
        @Query("stationto") stationTo: String?,
    ): Call<TrainTariffResponse>

}