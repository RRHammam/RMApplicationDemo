package com.example.rmapplication.model.estimatenumber

import com.google.gson.annotations.SerializedName

data class Fields(
    @SerializedName("@odata.etag")
    val etag: String,
    val Architect: String,
    val Attachments: Boolean,
    val AuthorLookupId: String,
    val City: String,
    val ContentType: String,
    val Created: String,
    val Date: String,
    val Edit: String,
    val EditorLookupId: String,
    val Estimate_x0020_Number: String,
    val FolderChildCount: String,
    val Green_x0020_Practices_x0020_and_: String,
    val ItemChildCount: String,
    val Lead_x0020_Estimator: String,
    val LinkTitle: String,
    val LinkTitleNoMenu: String,
    val Modified: String,
    val Operations_x0020_Manager: String,
    val Owner: String,
    val Request_x0020_Year: String,
    val Requested_x0020_By: String,
    val Scope: String,
    val State: String,
    val Status: String,
    val Title: String,
    val _ComplianceFlags: String,
    val _ComplianceTag: String,
    val _ComplianceTagUserId: String,
    val _ComplianceTagWrittenTime: String,
    val _UIVersionString: String,
    val id: String
)