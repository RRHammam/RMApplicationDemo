package com.example.rmapplication.model.policiesandprocedures

import com.example.rmapplication.model.CreatedBy
import com.example.rmapplication.model.LastModifiedBy
import com.google.gson.annotations.SerializedName

data class PoliciesAndProceduresResponse(
    @SerializedName("@odata.context")
    val context: String,
    @SerializedName("@odata.etag")
    val etag: String,
    val createdBy: CreatedBy,
    val createdDateTime: String,
    val description: String,
    val displayName: String,
    val eTag: String,
    val id: String,
    val items: MutableList<PoliciesAndProceduresItem>,
    @SerializedName("items@odata.context")
    val itemsContext: String,
    val lastModifiedBy: LastModifiedBy,
    val lastModifiedDateTime: String,
    val list: ListData,
    val name: String,
    val parentReference: ParentReferenceX,
    val webUrl: String
)