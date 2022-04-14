package com.robinsmorton.rmappandroid.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
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
): Parcelable
