package com.robinsmorton.rmappandroid.model

data class CorporateUser(
    val businessPhones: List<String?>,
    val displayName: String,
    val givenName: String?,
    val id: String?,
    val jobTitle: String?,
    val mail: String?,
    val mobilePhone: String?,
    val officeLocation: String?,
    val preferredLanguage: String?,
    val surname: String?,
    val userPrincipalName: String?
)
