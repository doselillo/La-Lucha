package com.doselfurioso.lalucha.domain.repository

import com.doselfurioso.lalucha.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTask(): Flow<Task?>
    suspend fun upsertTask(task: Task)
    suspend fun toggleTaskStatus()
}