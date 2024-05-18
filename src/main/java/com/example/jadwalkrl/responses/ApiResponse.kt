package com.example.jadwalkrl.responses

import com.example.jadwalkrl.models.Station
import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T,
)
