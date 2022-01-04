package com.example.rmapplication.model

import com.google.gson.annotations.SerializedName

data class Fields(
    @SerializedName("@odata.etag")
    val etag: String? = null,
    val Attachments: Boolean? = null,
    val AuthorLookupId: String? = null,
    val ContentType: String? = null,
    val Created: String? = null,
    val Edit: String? = null,
    val EditorLookupId: String? = null,
    val FolderChildCount: String? = null,
    val IsActive: Boolean? = null,
    val ItemChildCount: String? = null,
    val LinkTitle: String? = null,
    val LinkTitleNoMenu: String? = null,
    val Modified: String? = null,
    val Title: String? = null,
    val _ComplianceFlags: String? = null,
    val _ComplianceTag: String? = null,
    val _ComplianceTagUserId: String? = null,
    val _ComplianceTagWrittenTime: String? = null,
    val _UIVersionString: String? = null,
    val buttonimage: String? = null,
    val id: String? = null,
    val AppOrder: Int? = null
)