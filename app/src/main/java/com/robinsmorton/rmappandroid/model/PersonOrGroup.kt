package com.robinsmorton.rmappandroid.model

data class PersonOrGroup(
    val allowMultipleSelection: Boolean,
    val chooseFromType: String,
    val displayAs: String
)