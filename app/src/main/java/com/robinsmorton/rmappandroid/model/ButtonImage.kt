package com.robinsmorton.rmappandroid.model

data class ButtonImage(
    val fieldName: String,
    val fileName: String,
    val id: String,
    val serverRelativeUrl: String,
    val serverUrl: String,
    val thumbnailRenderer: ThumbnailRenderer,
    val type: String
)