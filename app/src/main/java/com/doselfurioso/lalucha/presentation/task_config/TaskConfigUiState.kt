package com.doselfurioso.lalucha.presentation.task_config

/**
 * Representa el estado de la pantalla de configuración.
 */
data class TaskConfigUiState(
    val title: String = "",
    val pendingImageUri: String? = null,
    val completedImageUri: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)