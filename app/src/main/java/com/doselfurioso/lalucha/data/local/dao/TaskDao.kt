package com.doselfurioso.lalucha.data.local.dao

import androidx.room.*
import com.doselfurioso.lalucha.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE id = 1")
    fun getTask(): Flow<TaskEntity?>

    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = NOT isCompleted, lastUpdated = :timestamp WHERE id = 1")
    suspend fun toggleTaskStatus(timestamp: Long = System.currentTimeMillis())
}