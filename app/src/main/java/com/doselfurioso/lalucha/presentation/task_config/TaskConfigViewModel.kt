package com.doselfurioso.lalucha.presentation.task_config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doselfurioso.lalucha.domain.model.Task
import com.doselfurioso.lalucha.domain.usecase.GetTaskUseCase
import com.doselfurioso.lalucha.domain.usecase.UpsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskConfigViewModel @Inject constructor(
    private val getTaskUseCase: GetTaskUseCase,
    private val upsertTaskUseCase: UpsertTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskConfigUiState())
    val uiState: StateFlow<TaskConfigUiState> = _uiState.asStateFlow()

    init {
        loadExistingTask()
    }

    private fun loadExistingTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getTaskUseCase().collect { task ->
                task?.let { t ->
                    _uiState.update {
                        it.copy(
                            title = t.title,
                            pendingImageUri = t.pendingImageUri,
                            completedImageUri = t.completedImageUri,
                            isLoading = false
                        )
                    }
                } ?: _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onPendingImageSelected(uri: String) {
        _uiState.update { it.copy(pendingImageUri = uri) }
    }

    fun onCompletedImageSelected(uri: String) {
        _uiState.update { it.copy(completedImageUri = uri) }
    }

    fun saveTask() {
        val currentState = _uiState.value
        if (currentState.title.isBlank() || currentState.pendingImageUri == null || currentState.completedImageUri == null) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa todos los campos") }
            return
        }

        viewModelScope.launch {
            try {
                val task = Task(
                    title = currentState.title,
                    pendingImageUri = currentState.pendingImageUri,
                    completedImageUri = currentState.completedImageUri
                )
                upsertTaskUseCase(task)
                _uiState.update { it.copy(isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al guardar: ${e.message}") }
            }
        }
    }
}