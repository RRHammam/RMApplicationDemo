package com.example.rmapplication.model.estimatenumber

import com.example.rmapplication.model.Column
import com.example.rmapplication.model.CreatedBy
import com.example.rmapplication.model.LastModifiedBy
import com.example.rmapplication.model.ListDescription
import com.example.rmapplication.model.ParentReference
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