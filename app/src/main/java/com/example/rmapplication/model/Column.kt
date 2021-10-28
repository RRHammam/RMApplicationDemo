package com.example.rmapplication.model

data class Column(
    val columnGroup: String,
    val dateTime: DateTime,
    val defaultValue: DefaultValue,
    val description: String,
    val displayName: String,
    val enforceUniqueValues: Boolean,
    val hidden: Boolean,
    val id: String,
    val indexed: Boolean,
    val lookup: Lookup,
    val name: String,
    val personOrGroup: PersonOrGroup,
    val readOnly: Boolean,
    val required: Boolean,
    val text: Text
)