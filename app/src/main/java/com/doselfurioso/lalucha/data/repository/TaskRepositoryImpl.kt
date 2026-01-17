package com.doselfurioso.lalucha.data.repository

import com.doselfurioso.lalucha.data.local.dao.TaskDao
import com.doselfurioso.lalucha.data.mapper.toDomain
import com.doselfurioso.lalucha.data.mapper.toEntity
import com.doselfurioso.lalucha.domain.model.Task
import com.doselfurioso.lalucha.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override fun getTask(): Flow<Task?> {
        return dao.getTask().map { it?.toDomain() }
    }

    override suspend fun upsertTask(task: Task) {
        dao.upsertTask(task.toEntity())
    }

    override suspend fun toggleTaskStatus() {
        dao.toggleTaskStatus()
    }
}