package com.robinsmorton.rmappandroid.model.jobrequest

import com.robinsmorton.rmappandroid.model.User

data class LastModifiedBy(
    val application: Application,
    val user: User
)