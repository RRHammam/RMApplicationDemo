package com.robinsmorton.rmappandroid.model.jobrequest

import com.google.gson.annotations.SerializedName

data class JobRequestResponse(
    @SerializedName("@odata.context")
    val context: String,
    @SerializedName("@odata.nextLink")
    val nextLink: String,
    val value: MutableList<JobRequestValue>
)