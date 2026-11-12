package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlinx.coroutines.delay

enum class EstadoJuego {
    BUSCANDO_OBJETIVO,
    EN_LA_MIRA,
    VICTORIA
}

@Composable
fun EjercicioFinalScreen() {
    var posNaveX by remember { mutableIntStateOf(0) }
    var posEnemigoX by remember { mutableIntStateOf(60) }
    var vidaEnemigo by remember { mutableIntStateOf(100) }
    var escudoActivo by remember { mutableStateOf(false) }
    var disparando by remember { mutableStateOf(false) }
    var enemigoGolpeado by remember { mutableStateOf(false) }

    LaunchedEffect(disparando) {
        if (disparando) {
            delay(350)
            disparando = false
        }
    }

    LaunchedEffect(enemigoGolpeado) {
        if (enemigoGolpeado) {
            delay(300)
            enemigoGolpeado = false
        }
    }

    val enLaMira = abs(posNaveX - posEnemigoX) <= 45
    val estadoJuego = when {
        vidaEnemigo <= 0 -> EstadoJuego.VICTORIA
        enLaMira -> EstadoJuego.EN_LA_MIRA
        else -> EstadoJuego.BUSCANDO_OBJETIVO
    }

    val offsetNaveAnimado by animateDpAsState(
        targetValue = posNaveX.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "offsetNave"
    )

    val offsetEnemigoAnimado by animateDpAsState(
        targetValue = posEnemigoX.dp,
        animationSpec = tween(durationMillis = 500),
        label = "offsetEnemigo"
    )

    val escalaNave by animateFloatAsState(
        targetValue = if (escudoActivo) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaNave"
    )

    val colorNave by animateColorAsState(
        targetValue = if (escudoActivo) Color(0xFFFFB300) else Color(0xFF00E5FF),
        animationSpec = tween(durationMillis = 500),
        label = "colorNave"
    )

    val colorEnemigo by animateColorAsState(
        targetValue = when {
            vidaEnemigo <= 0 -> Color(0xFF424242)
            enemigoGolpeado -> Color(0xFFFF1744)
            else -> Color(0xFFAB47BC)
        },
        animationSpec = tween(durationMillis = 250),
        label = "colorEnemigo"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // HUD Superior con AnimatedContent
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "SPACE DEFENDER",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(
                targetState = estadoJuego,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInVertically { -it / 2 }) togetherWith
                            (fadeOut(tween(250)) + slideOutVertically { it / 2 })
                },
                label = "hudEstado"
            ) { estado ->
                val (textoEstado, colorBadge) = when (estado) {
                    EstadoJuego.BUSCANDO_OBJETIVO -> "ALINEA LA NAVE CON EL ENEMIGO" to Color(0xFF546E7A)
                    EstadoJuego.EN_LA_MIRA -> "¡OBJETIVO EN LA MIRA! DISPARA" to Color(0xFFD32F2F)
                    EstadoJuego.VICTORIA -> "¡ENEMIGO DERROTADO! (+1000 PTS)" to Color(0xFF2E7D32)
                }
                Box(
                    modifier = Modifier
                        .background(colorBadge, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = textoEstado,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "HP Enemigo: $vidaEnemigo%",
                color = Color(0xFFCE93D8),
                fontWeight = FontWeight.SemiBold
            )
        }

        // Arena de Juego
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp)
                .background(Color(0xFF161B22), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF30363D), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Nave Enemiga
            androidx.compose.animation.AnimatedVisibility(
                visible = vidaEnemigo > 0,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut(tween(600)) + scaleOut(tween(600)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 36.dp)
                    .offset(x = offsetEnemigoAnimado)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(colorEnemigo, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👾", fontSize = 32.sp)
                }
            }

            // Rayo Láser
            androidx.compose.animation.AnimatedVisibility(
                visible = disparando,
                enter = fadeIn(tween(100)) + expandVertically(),
                exit = fadeOut(tween(200)),
                modifier = Modifier.offset(x = offsetNaveAnimado)
            ) {
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(180.dp)
                        .background(Color(0xFFFF5252), RoundedCornerShape(4.dp))
                )
            }

            // Nave del Jugador
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
                    .offset(x = offsetNaveAnimado)
                    .scale(escalaNave),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = escudoActivo,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .border(3.dp, Color(0xFFFFD54F), CircleShape)
                            .background(Color(0x33FFD54F), CircleShape)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(colorNave, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🚀", fontSize = 28.sp)
                }
            }
        }

        // Controles del Jugador
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { posNaveX = (posNaveX - 45).coerceAtLeast(-110) },
                    enabled = vidaEnemigo > 0
                ) {
                    Text("⬅️ Izq")
                }

                Button(
                    onClick = {
                        if (!disparando && vidaEnemigo > 0) {
                            disparando = true
                            if (enLaMira) {
                                vidaEnemigo = (vidaEnemigo - 25).coerceAtLeast(0)
                                enemigoGolpeado = true
                                if (vidaEnemigo > 0) {
                                    posEnemigoX = listOf(-90, -30, 30, 90)
                                        .filter { it != posEnemigoX }
                                        .random()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    enabled = vidaEnemigo > 0
                ) {
                    Text("🔥 Disparar")
                }

                Button(
                    onClick = { posNaveX = (posNaveX + 45).coerceAtMost(110) },
                    enabled = vidaEnemigo > 0
                ) {
                    Text("Der ➡️")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { escudoActivo = !escudoActivo }) {
                    Text(
                        text = if (escudoActivo) "🛡️ Escudo: ON" else "🛡️ Activar Escudo",
                        color = Color.White
                    )
                }

                if (vidaEnemigo <= 0) {
                    Button(
                        onClick = {
                            vidaEnemigo = 100
                            posNaveX = 0
                            posEnemigoX = 60
                            escudoActivo = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("🔄 Jugar de nuevo")
                    }
                }
            }
        }
    }
}