package com.doselfurioso.lalucha

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.doselfurioso.lalucha.presentation.task_config.TaskConfigScreen
import com.doselfurioso.lalucha.presentation.task_config.TaskConfigViewModel
import com.doselfurioso.lalucha.ui.theme.LaLuchaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaLuchaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: TaskConfigViewModel = hiltViewModel()
                    TaskConfigScreen(
                        viewModel = viewModel,
                        onSaveSuccess = {
                            Toast.makeText(this, "¡Lucha guardada! Añade el widget a tu pantalla", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    }
}