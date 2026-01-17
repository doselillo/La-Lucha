package com.doselfurioso.lalucha.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doselfurioso.lalucha.data.local.dao.TaskDao
import com.doselfurioso.lalucha.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}