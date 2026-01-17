package com.doselfurioso.lalucha.domain.usecase

import com.doselfurioso.lalucha.domain.model.Task
import com.doselfurioso.lalucha.domain.repository.TaskRepository
import javax.inject.Inject

class UpsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.upsertTask(task)
}