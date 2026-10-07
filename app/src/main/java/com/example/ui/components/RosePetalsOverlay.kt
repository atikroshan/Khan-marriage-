package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.sin
import kotlin.random.Random

data class Petal(
  val initialX: Float,
  val initialY: Float,
  val speed: Float,
  val size: Float,
  val swayFreq: Float,
  val swayAmplitude: Float,
  val rotationSpeed: Float,
  val color: Color
)

@Composable
fun RosePetalsRainOverlay(
  modifier: Modifier = Modifier,
  petalCount: Int = 30
) {
  val petals = remember {
    List(petalCount) {
      Petal(
        initialX = Random.nextFloat(),
        initialY = Random.nextFloat() * 1.4f - 0.4f,
        speed = Random.nextFloat() * 0.06f + 0.03f,
        size = Random.nextFloat() * 9f + 7f,
        swayFreq = Random.nextFloat() * 1.8f + 0.8f,
        swayAmplitude = Random.nextFloat() * 35f + 15f,
        rotationSpeed = Random.nextFloat() * 1.5f - 0.75f,
        color = when (Random.nextInt(5)) {
          0 -> Color(0xFFE84393) // Rose Pink
          1 -> Color(0xFFFF7675) // Soft Coral Red
          2 -> Color(0xFFD4AF37) // Gold Petal
          3 -> Color(0xFFFAB1A0) // Peach Pink
          else -> Color(0xFFC0392B) // Deep Crimson
        }
      )
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "rosePetals")
  val progress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(14000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "petalsProgress"
  )

  Canvas(modifier = modifier.fillMaxSize()) {
    val width = size.width
    val height = size.height

    petals.forEach { petal ->
      val currentY = ((petal.initialY + progress * petal.speed * 12f) % 1.4f - 0.2f) * height
      val timeVal = progress * 15f * petal.swayFreq + petal.initialX * 50f
      val currentX = (petal.initialX * width) + sin(timeVal) * petal.swayAmplitude
      val currentRotation = (progress * petal.rotationSpeed * 360f) + (petal.initialX * 360f)

      if (currentY in -60f..(height + 60f)) {
        rotate(currentRotation, pivot = Offset(currentX, currentY)) {
          drawOval(
            color = petal.color.copy(alpha = 0.78f),
            topLeft = Offset(currentX - petal.size / 2, currentY - petal.size),
            size = Size(petal.size, petal.size * 1.55f)
          )
          drawOval(
            color = Color.White.copy(alpha = 0.35f),
            topLeft = Offset(currentX - petal.size * 0.2f, currentY - petal.size * 0.7f),
            size = Size(petal.size * 0.38f, petal.size * 0.55f)
          )
        }
      }
    }
  }
}
