package com.doselfurioso.lalucha.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Int = 1,
    val title: String,
    val pendingImageUri: String,
    val completedImageUri: String,
    val isCompleted: Boolean,
    val lastUpdated: Long
)