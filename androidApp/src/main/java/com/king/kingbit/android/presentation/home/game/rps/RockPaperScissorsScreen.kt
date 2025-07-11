package com.king.kingbit.android.presentation.home.game.rps

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.king.kingbit.android.R
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.sqrt


enum class RPS {
    ROCK, PAPER, SCISSORS
}


@Stable
data class Particle(
    val id: Int,
    val type: MutableState<RPS>,
    val x: MutableState<Float>,
    val y: MutableState<Float>,
    val dx: MutableState<Float>,
    val dy: MutableState<Float>
)

@Composable
fun RockPaperScissorsGame(modifier: Modifier) {
    val particleSizeDp = 45.dp
    val particleSizePx = with(LocalDensity.current) { particleSizeDp.toPx() }
    val rockPainter = painterResource(id = R.drawable.ic_rock)
    val paperPainter = painterResource(id = R.drawable.ic_paper)
    val scissorsPainter = painterResource(id = R.drawable.ic_scissors)

    var gameWidth by remember { mutableFloatStateOf(0f) }
    var gameHeight by remember { mutableFloatStateOf(0f) }

    val particles = remember { mutableStateListOf<Particle>() }
    var gameKey by remember { mutableIntStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var winnerType by remember { mutableStateOf<RPS?>(null) }

    fun getWinnerPainter(type: RPS?): androidx.compose.ui.graphics.painter.Painter? = when (type) {
        RPS.ROCK -> rockPainter
        RPS.PAPER -> paperPainter
        RPS.SCISSORS -> scissorsPainter
        else -> null
    }

    fun getWinnerColor(type: RPS?): Color = when (type) {
        RPS.ROCK -> Color(0xFF90CAF9)
        RPS.PAPER -> Color(0xFFC8E6C9)
        RPS.SCISSORS -> Color(0xFFFFAB91)
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

            val typesPresent = listOf(rockCount, paperCount, scissorsCount).count { it > 0 }

            val twoTypeSpeedBoost = if (typesPresent == 2) 3.5f else 1f

            val minSpeedBoost = 1f
            val maxSpeedBoost = 3f
            val startBoostingBelow = 5
            val left = particles.size
            val globalSpeedBoost = if (left < startBoostingBelow)
                ((maxSpeedBoost - minSpeedBoost) * (startBoostingBelow - left) / (startBoostingBelow - 1).toFloat() + minSpeedBoost)
            else minSpeedBoost

            val magnetStrength = if (left <= 3) 0.19f else 0f
            val meanX = particles.map { it.x.value }.average().toFloat()
            val meanY = particles.map { it.y.value }.average().toFloat()

            particles.forEach { p ->
                val underdogBoost = when (p.type.value) {
                    RPS.ROCK -> if (rockCount == minOf(rockCount, paperCount, scissorsCount)) 1.2f else 1f
                    RPS.PAPER -> if (paperCount == minOf(rockCount, paperCount, scissorsCount)) 1.2f else 1f
                    RPS.SCISSORS -> if (scissorsCount == minOf(rockCount, paperCount, scissorsCount)) 1.2f else 1f
                }

                val speedMultiplier = underdogBoost * globalSpeedBoost * twoTypeSpeedBoost

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

            outer@ for (i in particles.indices.reversed()) {
                val p1 = particles.getOrNull(i) ?: continue
                for (j in (0 until i).reversed()) {
                    val p2 = particles.getOrNull(j) ?: continue

                    if (distance(p1.x.value, p1.y.value, p2.x.value, p2.y.value) < particleSizePx) {
                        val winningType = winner(p1.type.value, p2.type.value)

                        if (winningType != null) {
                            val (winnerParticle, loserParticle) = if (winningType == p1.type.value) {
                                p1 to p2
                            } else {
                                p2 to p1
                            }

                            val winnerTypeCount = particles.count { it.type.value == winnerParticle.type.value }

                            if (winnerTypeCount <= 2) {
                                loserParticle.type.value = winnerParticle.type.value
                            } else {
                                particles.remove(loserParticle)
                            }
                            break@outer
                        }
                    }
                }
            }

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

    val gradientBg = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFF1E0),
            Color(0xFFE0FFFB),
            Color(0xFFD0EAFF),
            Color(0xFFFFE3EC)
        ),
        start = Offset.Zero,
        end = Offset(1000f, 1800f)
    )

    Column(
        modifier
            .fillMaxSize()
            .background(gradientBg)
    ) {
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
                )
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = gameOver,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val animScale by animateFloatAsState(
                    targetValue = if (gameOver) 1.32f else 1.0f,
                    label = "gameOverScale"
                )
                val winnerGlow =
                    if (winnerType != null) Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.18f),
                            getWinnerColor(winnerType).copy(alpha = 0.62f),
                            Color.Transparent
                        ),
                        radius = 400f,
                        center = Offset(440f, 600f)
                    ) else Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))

                // --- MODIFICATION START ---
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(getWinnerColor(winnerType).copy(alpha = 0.96f))
                        // Make the entire screen clickable to restart the game
                        .clickable {
                            gameOver = false
                            particles.clear()
                            populateParticles()
                            gameKey += 1 // This restarts the LaunchedEffect
                        }
                        .padding(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Game Over\nWinner:",
                            fontSize = 28.sp,
                            color = Color(0xFF37474F),
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp, bottom = 3.dp)
                                .size((98 * animScale).dp)
                                .background(winnerGlow, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val winnerPainter = getWinnerPainter(winnerType)
                            if (winnerPainter != null) {
                                Image(
                                    painter = winnerPainter,
                                    contentDescription = null,
                                    modifier = Modifier.size(70.dp * animScale)
                                )
                            }
                        }
                        Text(
                            (winnerType?.name?.replaceFirstChar { it.uppercase() } ?: ""),
                            fontSize = 42.sp,
                            color = Color(0xFF263238),
                            modifier = Modifier.padding(top = 10.dp, bottom = 12.dp),
                        )
                        // Updated the instruction text
                        Text(
                            "Tap anywhere to play again!",
                            color = Color(0xFF5E636A),
                            fontSize = 19.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
                // --- MODIFICATION END ---
            }
        }
    }
}

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