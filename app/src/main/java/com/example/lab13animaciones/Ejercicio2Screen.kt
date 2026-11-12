package com.example.lab13animaciones

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
fun Ejercicio2Screen() {
    var esAzul by remember { mutableStateOf(true) }
    var usarSpring by remember { mutableStateOf(false) }

    val colorAnimado by animateColorAsState(
        targetValue = if (esAzul) Color(0xFF1E88E5) else Color(0xFF43A047),
        animationSpec = if (usarSpring) {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        } else {
            tween(durationMillis = 800)
        },
        label = "animacionColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(colorAnimado, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (esAzul) "Azul" else "Verde",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { esAzul = !esAzul }) {
            Text(text = "Cambiar color")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = { usarSpring = !usarSpring }) {
            Text(text = if (usarSpring) "Especificación: spring" else "Especificación: tween")
        }
    }
}