package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Blue300
import com.example.myapplication.ui.theme.Blue400
import com.example.myapplication.ui.theme.Blue600
import com.example.myapplication.ui.theme.Navy
import com.example.myapplication.ui.theme.Red600
import com.example.myapplication.ui.theme.TextMuted
import com.example.myapplication.ui.theme.TextPrimary

/**
 * Fondo oscuro con dos luces difuminadas (azul y carmesí): es lo que se "ve a través" del cristal.
 * El contenido queda dentro de las zonas seguras (barras del sistema y teclado).
 */
@Composable
fun GlassBackground(content: @Composable BoxScope.() -> Unit) {
    // Sin Surface, el texto sale negro por defecto: se fija el color de contenido aquí
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
        Box(Modifier.fillMaxSize().background(Navy)) {
            Box(
                Modifier
                    .size(340.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-110).dp, y = (-70).dp)
                    .blur(90.dp, BlurredEdgeTreatment.Unbounded)
                    .background(Blue600.copy(alpha = 0.55f), CircleShape)
            )
            Box(
                Modifier
                    .size(320.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 100.dp, y = 80.dp)
                    .blur(90.dp, BlurredEdgeTreatment.Unbounded)
                    .background(Red600.copy(alpha = 0.5f), CircleShape)
            )
            Box(Modifier.fillMaxSize().safeDrawingPadding(), content = content)
        }
    }
}

/**
 * Tarjeta de cristal: base oscura (asegura contraste del texto sobre las luces),
 * brillo en degradado y borde fino de luz. Si recibe [onClick] se comporta como botón.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    tint: Color = Color.Transparent,
    padding: Dp = 20.dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(shape)
            .background(Navy.copy(alpha = 0.35f))
            .background(tint)
            .background(Brush.linearGradient(listOf(Color.White.copy(alpha = 0.16f), Color.White.copy(alpha = 0.05f))))
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color.White.copy(alpha = 0.40f), Color.White.copy(alpha = 0.08f))),
                shape
            )
            .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
            .padding(padding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun glassFieldColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = Color.White.copy(alpha = 0.06f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.06f),
    focusedIndicatorColor = Blue400,
    unfocusedIndicatorColor = Color(0xFF8499BF),
    focusedLabelColor = Blue300,
    unfocusedLabelColor = TextMuted,
    cursorColor = Blue300
)
