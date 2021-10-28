package com.example.rmapplication.model

data class PersonOrGroup(
    val allowMultipleSelection: Boolean,
    val chooseFromType: String,
    val displayAs: String
)