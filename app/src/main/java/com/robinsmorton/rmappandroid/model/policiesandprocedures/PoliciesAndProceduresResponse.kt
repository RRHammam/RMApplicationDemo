package com.robinsmorton.rmappandroid.model.policiesandprocedures

import com.robinsmorton.rmappandroid.model.CreatedBy
import com.robinsmorton.rmappandroid.model.LastModifiedBy
import com.google.gson.annotations.SerializedName
import com.robinsmorton.rmappandroid.model.ContentType
import com.robinsmorton.rmappandroid.model.Fields
import com.robinsmorton.rmappandroid.model.ParentReference
import com.robinsmorton.rmappandroid.model.Value

data class PoliciesAndProceduresResponse(
    @SerializedName("@odata.context")
    val context: String,
    val value: MutableList<PoliciesAndProceduresItem>
)