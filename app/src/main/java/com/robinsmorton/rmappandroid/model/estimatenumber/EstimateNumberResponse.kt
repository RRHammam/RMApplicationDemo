package com.robinsmorton.rmappandroid.model.estimatenumber

import com.robinsmorton.rmappandroid.model.Column
import com.robinsmorton.rmappandroid.model.CreatedBy
import com.robinsmorton.rmappandroid.model.LastModifiedBy
import com.robinsmorton.rmappandroid.model.ListDescription
import com.robinsmorton.rmappandroid.model.ParentReference
import com.google.gson.annotations.SerializedName

data class EstimateNumberResponse(
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