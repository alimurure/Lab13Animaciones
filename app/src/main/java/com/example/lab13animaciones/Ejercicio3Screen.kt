package com.example.lab13animaciones

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3Screen() {
    var expandido by remember { mutableStateOf(false) }
    var offsetAntesDeFondo by remember { mutableStateOf(true) }

    val tamano by animateDpAsState(
        targetValue = if (expandido) 180.dp else 100.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "animacionTamano"
    )

    val offsetX by animateDpAsState(
        targetValue = if (expandido) 60.dp else (-60).dp,
        label = "animacionOffsetX"
    )

    val offsetY by animateDpAsState(
        targetValue = if (expandido) 30.dp else (-20).dp,
        label = "animacionOffsetY"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val boxModifier = if (offsetAntesDeFondo) {
            Modifier
                .offset(x = offsetX, y = offsetY)
                .size(tamano)
                .background(Color(0xFF6A1B9A), shape = RoundedCornerShape(16.dp))
        } else {
            Modifier
                .size(tamano)
                .background(Color(0xFF6A1B9A), shape = RoundedCornerShape(16.dp))
                .offset(x = offsetX, y = offsetY)
        }

        Box(
            modifier = boxModifier,
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Caja", color = Color.White)
        }

        Spacer(modifier = Modifier.height(80.dp))

        Button(onClick = { expandido = !expandido }) {
            Text(text = if (expandido) "Reducir y volver" else "Mover y agrandar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = { offsetAntesDeFondo = !offsetAntesDeFondo }) {
            Text(
                text = if (offsetAntesDeFondo) {
                    "Orden: offset -> background"
                } else {
                    "Orden: background -> offset"
                }
            )
        }
    }
}