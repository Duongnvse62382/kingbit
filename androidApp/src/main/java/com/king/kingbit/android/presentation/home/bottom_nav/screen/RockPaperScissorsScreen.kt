package com.king.kingbit.android.presentation.home.bottom_nav.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt
import com.king.kingbit.android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import kotlin.random.Random

// Enum for Rock-Paper-Scissors
enum class RPS {
    ROCK, PAPER, SCISSORS
}

// Utility function for mutableFloatStateOf using mutableStateOf for Float type
fun mutableFloatStateOf(value: Float) = mutableStateOf(value)

@Composable
fun RockPaperScissorsScreen (modifier: Modifier = Modifier) {
   RockPaperScissorsGame(modifier)
}


@Stable
data class Particle(
    val id: Int, // stable key
    val type: MutableState<RPS>,
    val x: MutableState<Float>,
    val y: MutableState<Float>,
    val dx: MutableState<Float>,
    val dy: MutableState<Float>
)

@Composable
fun RockPaperScissorsGame(modifier: Modifier) {
    val particleSizeDp = 48.dp
    val particleSizePx = with(LocalDensity.current) { particleSizeDp.toPx() }
    val rockPainter = painterResource(id = R.drawable.ic_apple)
    val paperPainter = painterResource(id = R.drawable.ic_google)
    val scissorsPainter = painterResource(id = R.drawable.ic_fb)

    var gameWidth by remember { mutableStateOf(0f) }
    var gameHeight by remember { mutableStateOf(0f) }

    val particles = remember { mutableStateListOf<Particle>() }
    var gameKey by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var winnerType by remember { mutableStateOf<RPS?>(null) }

    fun getWinnerEmoji(type: RPS?): String = when (type) {
        RPS.ROCK -> "🪨"
        RPS.PAPER -> "📄"
        RPS.SCISSORS -> "✂️"
        null -> ""
    }

    fun getWinnerColor(type: RPS?): Color = when (type) {
        RPS.ROCK -> Color(0xFF90CAF9) // blue
        RPS.PAPER -> Color(0xFFC8E6C9) // green
        RPS.SCISSORS -> Color(0xFFFFAB91) // orange
        null -> Color(0xAA222222)
    }

    fun populateParticles() {
        winnerType = null
        if (gameWidth > 0 && gameHeight > 0 && particles.isEmpty()) {
            repeat(10) {
                particles.add(
                    randomParticle(
                        RPS.ROCK,
                        it,
                        x = (0..(gameWidth - particleSizePx).toInt()).random().toFloat(),
                        y = (0..(gameHeight - particleSizePx).toInt()).random().toFloat()
                    )
                )
            }
            repeat(10) {
                particles.add(
                    randomParticle(
                        RPS.PAPER,
                        100 + it,
                        x = (0..(gameWidth - particleSizePx).toInt()).random().toFloat(),
                        y = (0..(gameHeight - particleSizePx).toInt()).random().toFloat()
                    )
                )
            }
            repeat(10) {
                particles.add(
                    randomParticle(
                        RPS.SCISSORS,
                        200 + it,
                        x = (0..(gameWidth - particleSizePx).toInt()).random().toFloat(),
                        y = (0..(gameHeight - particleSizePx).toInt()).random().toFloat()
                    )
                )
            }
        }
    }
    LaunchedEffect(gameWidth, gameHeight) { populateParticles() }

    LaunchedEffect(gameKey) {
        while (true) {
            if (gameOver) break
            val rockCount = particles.count { it.type.value == RPS.ROCK }
            val paperCount = particles.count { it.type.value == RPS.PAPER }
            val scissorsCount = particles.count { it.type.value == RPS.SCISSORS }

            val minSpeedBoost = 1f
            val maxSpeedBoost = 3f
            val startBoostingBelow = 5
            val left = particles.size
            val globalSpeedBoost = if (left < startBoostingBelow)
                ((maxSpeedBoost - minSpeedBoost) * (startBoostingBelow - left) / (startBoostingBelow - 1).toFloat() + minSpeedBoost)
            else minSpeedBoost

            // Endgame magnet: if only a few left, nudge toward center of mass so a winner is guaranteed
            val magnetStrength = if (left <= 3) 0.19f else 0f
            val meanX = particles.map { it.x.value }.average().toFloat()
            val meanY = particles.map { it.y.value }.average().toFloat()

            particles.forEach { p ->
                // Speed boost for underdog team (optional, reduced effect, can be removed if not desired)
                val underdogBoost = when (p.type.value) {
                    RPS.ROCK -> if (rockCount == minOf(
                            rockCount,
                            paperCount,
                            scissorsCount
                        )
                    ) 1.2f else 1f

                    RPS.PAPER -> if (paperCount == minOf(
                            rockCount,
                            paperCount,
                            scissorsCount
                        )
                    ) 1.2f else 1f

                    RPS.SCISSORS -> if (scissorsCount == minOf(
                            rockCount,
                            paperCount,
                            scissorsCount
                        )
                    ) 1.2f else 1f
                }
                val speedMultiplier = underdogBoost * globalSpeedBoost

                // Magnet attraction (endgame): nudge toward center of mass
                if (magnetStrength > 0f) {
                    val dxToCenter = meanX - p.x.value
                    val dyToCenter = meanY - p.y.value
                    p.x.value += dxToCenter * magnetStrength
                    p.y.value += dyToCenter * magnetStrength
                }

                p.x.value += p.dx.value * speedMultiplier
                p.y.value += p.dy.value * speedMultiplier
                if (p.x.value < 0) {
                    p.x.value = 0f; p.dx.value *= -1
                }
                if (p.x.value > gameWidth - particleSizePx) {
                    p.x.value = gameWidth - particleSizePx; p.dx.value *= -1
                }
                if (p.y.value < 0) {
                    p.y.value = 0f; p.dy.value *= -1
                }
                if (p.y.value > gameHeight - particleSizePx) {
                    p.y.value = gameHeight - particleSizePx; p.dy.value *= -1
                }
            }

            // --- FIXED ELIMINATION LOGIC ---
            var removed = false
            outer@ for (i in particles.indices) {
                val p1 = particles.getOrNull(i) ?: continue
                for (j in (i + 1) until particles.size) {
                    val p2 = particles.getOrNull(j) ?: continue
                    if (distance(p1.x.value, p1.y.value, p2.x.value, p2.y.value) < particleSizePx) {
                        val w = winner(p1.type.value, p2.type.value)
                        if (w != null) {
                            if (w == p1.type.value) {
                                particles.remove(p2)
                            } else {
                                particles.remove(p1)
                            }
                            removed = true
                            break@outer
                        }
                    }
                }
            }
            // Winner check
            val countRock = particles.count { it.type.value == RPS.ROCK }
            val countPaper = particles.count { it.type.value == RPS.PAPER }
            val countScissors = particles.count { it.type.value == RPS.SCISSORS }
            val total = particles.size
            winnerType = when {
                countRock == total && total > 0 -> RPS.ROCK
                countPaper == total && total > 0 -> RPS.PAPER
                countScissors == total && total > 0 -> RPS.SCISSORS
                else -> null
            }
            if (winnerType != null) gameOver = true
            delay(16)
        }
    }

    val scoreboardShape = RoundedCornerShape(18.dp)
    val scoreboardBg = Brush.horizontalGradient(listOf(Color(0xFFEEEEEE), Color(0xFFB3E5FC)))

    Column(
        modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2193b0), // blue
                        Color(0xFF6dd5ed)  // light blue
                    )
                )
            )
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(scoreboardBg, scoreboardShape)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                gameOver = false
                particles.clear()
                populateParticles()
                gameKey += 1 // Restart
            }, modifier = Modifier.shadow(4.dp, RoundedCornerShape(12.dp))) {
                Text("🔄 Reset", fontSize = 16.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "🪨 ",
                    fontSize = 20.sp
                ); Text(
                ": ${particles.count { it.type.value == RPS.ROCK }}",
                fontSize = 18.sp,
                color = Color(0xFF1565C0)
            )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "📄 ",
                    fontSize = 20.sp
                ); Text(
                ": ${particles.count { it.type.value == RPS.PAPER }}",
                fontSize = 18.sp,
                color = Color(0xFF388E3C)
            )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "✂️ ",
                    fontSize = 20.sp
                ); Text(
                ": ${particles.count { it.type.value == RPS.SCISSORS }}",
                fontSize = 18.sp,
                color = Color(0xFFD84315)
            )
            }
        }

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .onGloballyPositioned {
                    gameWidth = it.size.width.toFloat()
                    gameHeight = it.size.height.toFloat()
                }
        ) {
            particles.forEach { p ->
                val painter = when (p.type.value) {
                    RPS.ROCK -> rockPainter
                    RPS.PAPER -> paperPainter
                    RPS.SCISSORS -> scissorsPainter
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(particleSizeDp)
                        .offset { IntOffset(p.x.value.toInt(), p.y.value.toInt()) }
                        .shadow(8.dp, shape = RoundedCornerShape(10))
                )
            }
            // Place AnimatedVisibility directly inside the Box scope, not in Column/ColumnScope
            androidx.compose.animation.AnimatedVisibility(
                visible = gameOver,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val animScale by animateFloatAsState(
                    targetValue = if (gameOver) 1.1f else 0.8f,
                    label = "gameOverScale"
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(getWinnerColor(winnerType).copy(alpha = 0.85f))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = buildString {
                                append("Game Over\nWinner: ")
                                append(getWinnerEmoji(winnerType))
                                append(" ")
                                append(winnerType?.name?.replaceFirstChar { it.uppercase() } ?: "")
                            },
                            fontSize = 38.sp * animScale,
                            color = Color(0xFF263238),
                            modifier = Modifier,
                        )
                        Spacer(Modifier.height(18.dp))
                        Text(
                            "Tap reset to play again!",
                            color = Color(0xFF37474F),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

// The winning rate of scissors will be very high :)
fun randomParticle(type: RPS, id: Int, x: Float, y: Float): Particle {
    return Particle(
        id = id,
        type = mutableStateOf(type),
        x = mutableFloatStateOf(x),
        y = mutableFloatStateOf(y),
        dx = mutableFloatStateOf(listOf(-2f, -1.5f, 1.5f, 2f).random()),
        dy = mutableFloatStateOf(listOf(-2f, -1.5f, 1.5f, 2f).random())
    )
}


fun winner(a: RPS, b: RPS): RPS? {
    return when {
        a == b -> null
        a == RPS.ROCK && b == RPS.SCISSORS -> a
        a == RPS.SCISSORS && b == RPS.PAPER -> a
        a == RPS.PAPER && b == RPS.ROCK -> a
        b == RPS.ROCK && a == RPS.SCISSORS -> b
        b == RPS.SCISSORS && a == RPS.PAPER -> b
        b == RPS.PAPER && a == RPS.ROCK -> b
        else -> null
    }
}

fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
}