package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("@odata.etag")
    val etag: String? = null,
    val contentType: ContentType? = null,
    val createdBy: CreatedBy? = null,
    val createdDateTime: String? = null,
    val eTag: String? = null,
    val fields: Fields? = null,
    @SerializedName("fields@odata.context")
    val fieldsUrl: String? = null,
    val id: String? = null,
    val lastModifiedBy: LastModifiedBy? = null,
    val lastModifiedDateTime: String? = null,
    val parentReference: ParentReference? = null,
    val webUrl: String? = null
)