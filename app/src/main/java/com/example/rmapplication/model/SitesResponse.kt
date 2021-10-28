package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class SitesResponse(
    @SerializedName("@odata.context")
    val data: String,
    val value: List<Site>
)