package com.robinsmorton.rmappandroid.model.jobrequest

import com.google.gson.annotations.SerializedName

data class JobRequestValue(
    @SerializedName("@odata.etag")
    val etag: String,
    val contentType: ContentType,
    val createdDateTime: String,
    val eTag: String,
    val fields: Fields,
    @SerializedName("fields@odata.context")
    val context: String,
    val id: String,
    val lastModifiedBy: LastModifiedBy,
    val lastModifiedDateTime: String,
    val parentReference: ParentReference,
    val webUrl: String
)