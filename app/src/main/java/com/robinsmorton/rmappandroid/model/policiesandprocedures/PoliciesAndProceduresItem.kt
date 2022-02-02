package com.robinsmorton.rmappandroid.model.policiesandprocedures

import com.robinsmorton.rmappandroid.model.CreatedBy
import com.robinsmorton.rmappandroid.model.LastModifiedBy
import com.google.gson.annotations.SerializedName

data class PoliciesAndProceduresItem(
    @SerializedName ("@odata.etag")
    val etag: String,
    val contentType: ContentType,
    val createdBy: CreatedBy,
    val createdDateTime: String,
    val eTag: String,
    val fields: Fields,
    @SerializedName ("fields@odata.context")
    val context: String,
    val id: String,
    val lastModifiedBy: LastModifiedBy,
    val lastModifiedDateTime: String,
    val parentReference: ParentReference,
    val webUrl: String
)