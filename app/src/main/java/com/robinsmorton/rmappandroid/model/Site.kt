package com.robinsmorton.rmappandroid.model

data class Site(
    val displayName: String,
    val id: String,
    val sharepointIds: SharepointIds,
    val siteCollection: SiteCollection,
    val webUrl: String
)