package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("@odata.etag")
    val etag: String,
    val contentType: ContentType,
    val createdBy: CreatedBy,
    val createdDateTime: String,
    val eTag: String,
    val fields: Fields,
    @SerializedName("fields@odata.context")
    val fieldsUrl: String,
    val id: String,
    val lastModifiedBy: LastModifiedBy,
    val lastModifiedDateTime: String,
    val parentReference: ParentReference,
    val webUrl: String
)