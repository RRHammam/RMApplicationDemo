package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName

data class CorporateDirectoryResponse(
    @SerializedName("@odata.context")
    val metaDataUrl: String,
    @SerializedName("@odata.nextLink")
    val usersUrl: String,
    val value: List<CorporateUser>
)