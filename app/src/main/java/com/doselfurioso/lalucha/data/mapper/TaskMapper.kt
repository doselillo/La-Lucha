package com.doselfurioso.lalucha.data.mapper

import com.doselfurioso.lalucha.data.local.entity.TaskEntity
import com.doselfurioso.lalucha.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        pendingImageUri = pendingImageUri,
        completedImageUri = completedImageUri,
        isCompleted = isCompleted,
        lastUpdated = lastUpdated
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = if (id == 0) 1 else id,
        title = title,
        pendingImageUri = pendingImageUri,
        completedImageUri = completedImageUri,
        isCompleted = isCompleted,
        lastUpdated = lastUpdated
    )
}