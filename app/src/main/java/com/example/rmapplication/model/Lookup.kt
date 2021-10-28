package com.example.rmapplication.model

data class Lookup(
    val allowMultipleValues: Boolean,
    val allowUnlimitedLength: Boolean,
    val columnName: String,
    val listId: String,
    val primaryLookupColumnId: String
)