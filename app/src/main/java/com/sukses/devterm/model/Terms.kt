package com.sukses.devterm.model

import com.google.firebase.Timestamp

data class Terms(
    val userId: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val documentId: String = "",
)
