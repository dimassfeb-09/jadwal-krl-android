package com.example.jadwalkrl.models

import com.google.gson.annotations.SerializedName

data class StationResponse(
    val status: Int,
    val message: String,
    val data: List<Station>
)

data class StationScheduleResponse(
    val status: Int,
    val message: String,
    val data: List<ScheduleStation>
)

data class Station(
    @SerializedName("sta_id") val stationId: String,
    @SerializedName("sta_name") val stationName: String,
    @SerializedName("group_wil") val groupWilayah: Int,
    @SerializedName("fg_enable") val fgEnable: Int,
)

data class ScheduleStation(
    @SerializedName("train_id") val trainId: String,
    @SerializedName("ka_name") val kaName: String,
    @SerializedName("route_name") val routeName: String,
    @SerializedName("dest") val destination: String,
    @SerializedName("time_est") val timeEstimation: String,
    @SerializedName("color") val color: String,
    @SerializedName("dest_time") val destinationTime: String,
)