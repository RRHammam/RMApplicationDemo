package com.example.rmapplication.model.policiesandprocedures

import com.google.gson.annotations.SerializedName

data class Fields(
    @SerializedName("@odata.etag")
    var eTag: String,
    val AuthorLookupId: String,
    val ContentType: String,
    val Created: String,
    val DocIcon: String,
    val Edit: String,
    val EditorLookupId: String,
    val FileLeafRef: String,
    val FileSizeDisplay: String,
    val FolderChildCount: String,
    val ItemChildCount: String,
    val LinkFilename: String,
    val LinkFilenameNoMenu: String,
    val Modified: String,
    val ParentLeafNameLookupId: String,
    val ParentVersionStringLookupId: String,
    val Title: String,
    val _CheckinComment: String,
    val _CommentCount: String,
    val _ComplianceFlags: String,
    val _ComplianceTag: String,
    val _ComplianceTagUserId: String,
    val _ComplianceTagWrittenTime: String,
    val _DisplayName: String,
    val _LikeCount: String,
    val _ModerationComments: String,
    val _ModerationStatus: Int,
    val _UIVersionString: String,
    val id: String
)