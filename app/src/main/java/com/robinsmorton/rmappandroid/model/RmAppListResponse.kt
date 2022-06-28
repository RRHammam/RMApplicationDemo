package com.robinsmorton.rmappandroid.model

import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class RmAppListResponse(
    @SerializedName("@odata.context")
    val data: String,
    @SerializedName("@odata.etag")
    val etag: String,
    @SerializedName("columns@odata.context")
    val columnsUrl: String,
    val createdBy: CreatedBy,
    val createdDateTime: String,
    val description: String,
    val displayName: String,
    val eTag: String,
    val id: String,
    val items: MutableList<Item>,
    @SerializedName("items@odata.context")
    val itemsUrl: String,
    val columns: List<Column>,
    val lastModifiedBy: LastModifiedBy,
    val lastModifiedDateTime: String,
    val list: ListDescription,
    val name: String,
    val parentReference: ParentReference,
    val webUrl: String
)