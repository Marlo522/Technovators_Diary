package com.example.technovatorsdiary.model

import java.util.Date

data class JournalEntry(
    val title: String = "",
    val date: Date? = null,
    val text: String = "",
    val id: String = ""  // Firestore doc ID (for update)
)

