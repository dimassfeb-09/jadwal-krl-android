package com.example.jadwalkrl.models

import com.google.gson.annotations.SerializedName


data class TrainTariffResponse(
    val status: Int,
    val data: List<TariffTrain>
)

data class TrainScheduleResponse(
    val status: Int,
    val data: List<ScheduleTrain>
)

data class ScheduleTrain(
    @SerializedName("train_id") val trainId: String,
    @SerializedName("ka_name") val kaName: String,
    @SerializedName("station_id") val stationId: String,
    @SerializedName("station_name") val stationName: String,
    @SerializedName("time_est") val timeEstimation: String,
    @SerializedName("transit_station") val transitStation: Boolean,
    @SerializedName("color") val color: String,
    @SerializedName("transit") val transit: Any
)

data class TariffTrain(
    @SerializedName("sta_code_from") val staCodeFrom: String?,
    @SerializedName("sta_name_from") val staNameFrom: String?,
    @SerializedName("sta_code_to") val staCodeTo: String?,
    @SerializedName("staNameTo") val staNameTo: String?,
    @SerializedName("fare") var fare: Int?,
    @SerializedName("distance") var distance: String?
)