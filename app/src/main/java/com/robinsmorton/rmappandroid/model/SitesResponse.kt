package com.robinsmorton.rmappandroid.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class SitesResponse(
    @SerializedName("@odata.context")
    val data: String,
    val value: List<Site>
)