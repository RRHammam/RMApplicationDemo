package com.robinsmorton.rmappandroid.model.estimatenumber

import com.google.gson.annotations.SerializedName
import com.robinsmorton.rmappandroid.model.Value

data class EstimateNumbersResponse(
    @SerializedName("@odata.context")
    val context: String,
    @SerializedName("@odata.nextLink")
    val nextLink: String,
    val value: MutableList<Value>
)