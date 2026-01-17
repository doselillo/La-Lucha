package com.doselfurioso.lalucha.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.doselfurioso.lalucha.domain.repository.TaskRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import java.io.InputStream

class TaskWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        ).taskRepository()

        val task = repository.getTask().first()
        
        // Cargamos el bitmap fuera de provideContent para mayor estabilidad y rendimiento
        val bitmap = task?.let { 
            loadOptimizedBitmap(context, if (it.isCompleted) it.completedImageUri else it.pendingImageUri) 
        }

        provideContent {
            if (task != null) {
                TaskWidgetContent(
                    title = task.title,
                    bitmap = bitmap,
                    isCompleted = task.isCompleted
                )
            } else {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Configura tu tarea en la App")
                }
            }
        }
    }

    @Composable
    private fun TaskWidgetContent(title: String, bitmap: Bitmap?, isCompleted: Boolean) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color.Black)), // Fondo negro para los bordes si la imagen no encaja
            contentAlignment = Alignment.BottomCenter // Alineamos el contenido (texto/botón) abajo
        ) {
            // Imagen de fondo con Fit para verla completa
            bitmap?.let {
                Image(
                    provider = ImageProvider(it),
                    contentDescription = null,
                    modifier = GlanceModifier.fillMaxSize(),
                    contentScale = ContentScale.Fit // <--- Cambiado de Crop a Fit para evitar el recorte
                )
            }

            // Franja de información en la parte inferior
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(ColorProvider(Color(0x99000000))), // Fondo negro semi-transparente un poco más oscuro
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = TextStyle(color = ColorProvider(Color.White))
                )
                Spacer(GlanceModifier.height(4.dp))
                Button(
                    text = if (isCompleted) "¡Hecho!" else "Marcar como hecho",
                    onClick = actionRunCallback<ToggleTaskAction>()
                )
            }
        }
    }

    /**
     * Decodifica una imagen desde una URI de forma extremadamente eficiente para evitar 
     * errores de memoria (RemoteViews limits) en los Widgets.
     */
    private fun loadOptimizedBitmap(context: Context, uriString: String): Bitmap? {
        return try {
            val uri = Uri.parse(uriString)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }

            // Subimos a 450px para mayor nitidez
            options.inSampleSize = calculateInSampleSize(options, 450, 450)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = Bitmap.Config.RGB_565

            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}

class TaskWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TaskWidget()
}

class ToggleTaskAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val repository = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        ).taskRepository()
        
        repository.toggleTaskStatus()
        
        // Forzamos la actualización inmediata del Widget tras el cambio de estado
        TaskWidget().update(context, glanceId)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun taskRepository(): TaskRepository
}