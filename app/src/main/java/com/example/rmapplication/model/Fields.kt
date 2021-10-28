package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName

data class Fields(
    @SerializedName("@odata.etag")
    val etag: String,
    val Attachments: Boolean,
    val AuthorLookupId: String,
    val ContentType: String,
    val Created: String,
    val Edit: String,
    val EditorLookupId: String,
    val FolderChildCount: String,
    val IsActive: Boolean,
    val ItemChildCount: String,
    val LinkTitle: String,
    val LinkTitleNoMenu: String,
    val Modified: String,
    val Title: String,
    val _ComplianceFlags: String,
    val _ComplianceTag: String,
    val _ComplianceTagUserId: String,
    val _ComplianceTagWrittenTime: String,
    val _UIVersionString: String,
    val buttonimage: String,
    val id: String
)