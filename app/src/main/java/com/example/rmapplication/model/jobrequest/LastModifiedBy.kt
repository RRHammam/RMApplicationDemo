package com.example.rmapplication.model.jobrequest

import com.example.rmapplication.model.User

data class LastModifiedBy(
    val application: Application,
    val user: User
)