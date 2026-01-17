package com.doselfurioso.lalucha.domain.usecase

import com.doselfurioso.lalucha.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke() = repository.getTask()
}