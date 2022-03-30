package com.robinsmorton.rmappandroid.model.estimatenumber

import com.robinsmorton.rmappandroid.model.Application

data class LastModifiedBy(
    val application: Application,
    val user: User
)