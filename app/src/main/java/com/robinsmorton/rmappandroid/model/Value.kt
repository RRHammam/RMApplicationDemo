package com.robinsmorton.rmappandroid.model

import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName ("@odata.etag")
    val etag: String = "",
    val contentType: ContentType? = null,
    val createdBy: CreatedBy? = null,
    val createdDateTime: String = "",
    val eTag: String = "",
    val fields: Fields? = null,
    @SerializedName ("fields@odata.context")
    val context: String = "",
    val id: String = "",
    val lastModifiedBy: LastModifiedBy? = null,
    val lastModifiedDateTime: String = "",
    val parentReference: ParentReference? =  null,
    val webUrl: String = ""
)