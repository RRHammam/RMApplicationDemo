package com.robinsmorton.rmappandroid.model

import com.google.gson.annotations.SerializedName

data class ApplicationsListResponse(
    @SerializedName("@odata.context")
    val context: String,
    val value: MutableList<Value>
)