package com.robinsmorton.rmappandroid.model

data class Text(
    val allowMultipleLines: Boolean,
    val appendChangesToExistingText: Boolean,
    val linesForEditing: Int,
    val maxLength: Int
)