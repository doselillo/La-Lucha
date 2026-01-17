package com.doselfurioso.lalucha.domain.usecase

import com.doselfurioso.lalucha.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskStatusUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke() = repository.toggleTaskStatus()
}