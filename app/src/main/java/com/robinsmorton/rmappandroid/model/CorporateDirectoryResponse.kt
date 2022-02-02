package com.robinsmorton.rmappandroid.model

import com.google.gson.annotations.SerializedName

data class CorporateDirectoryResponse(
    @SerializedName("@odata.context")
    val metaDataUrl: String,
    @SerializedName("@odata.nextLink")
    val nextUsersUrl: String = "",
    val value: MutableList<CorporateUser>
)