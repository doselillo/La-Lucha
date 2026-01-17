package com.doselfurioso.lalucha.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val pendingImageUri: String,
    val completedImageUri: String,
    val isCompleted: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)