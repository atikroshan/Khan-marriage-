package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.AppDatabase
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMedium
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldOutline
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextSand

@Composable
fun KhanHeader(
  modifier: Modifier = Modifier,
  badgeTitle: String? = null,
  badgeSubtitle: String? = null,
  avatarUrl: String? = null,
  onAvatarClick: (() -> Unit)? = null,
  onBack: (() -> Unit)? = null
) {
  val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Macro wedding hands banner with edge-to-edge bleed to very top edge of screen
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(148.dp + statusBarTop)
    ) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(R.drawable.ic_jodi_hands_banner)
          .crossfade(true)
          .build(),
        contentDescription = "Royal bride and groom hands holding with mehndi and ring",
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
      )

      // Professional smooth bottom feather gradient
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.verticalGradient(
              0f to Color.Transparent,
              0.68f to Color.Transparent,
              0.86f to RoyalBlack.copy(alpha = 0.45f),
              1f to RoyalBlack
            )
          )
      )

      // Subtle soft side vignette
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.horizontalGradient(
              0f to RoyalBlack.copy(alpha = 0.45f),
              0.16f to Color.Transparent,
              0.84f to Color.Transparent,
              1f to RoyalBlack.copy(alpha = 0.45f)
            )
          )
      )

      // Prominent Back Button at top left (with status bar top padding for edge-to-edge safe area)
      if (onBack != null) {
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .statusBarsPadding()
            .padding(top = 8.dp, start = 12.dp)
            .size(42.dp)
            .clip(CircleShape)
            .background(RoyalBlack.copy(alpha = 0.88f))
            .border(1.5.dp, GoldPrimary, CircleShape)
            .clickable { onBack() }
            .testTag("header_back_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = GoldLight,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      // Optional avatar icon at top right (like in Image 1, with status bar top padding)
      if (avatarUrl != null) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .statusBarsPadding()
            .padding(top = 8.dp, end = 16.dp)
            .size(52.dp)
            .clip(CircleShape)
            .border(2.dp, GoldPrimary, CircleShape)
            .border(4.dp, GoldPrimary.copy(alpha = 0.25f), CircleShape)
            .background(RoyalBlack)
            .clickable(enabled = onAvatarClick != null) { onAvatarClick?.invoke() }
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(avatarUrl)
              .crossfade(true)
              .build(),
            contentDescription = "Selected candidate portrait",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }

    // App Branding Typography (shifted down with scaled fonts)
    Text(
      text = "KHAN",
      fontFamily = FontFamily.Serif,
      fontWeight = FontWeight.Black,
      fontSize = 31.sp,
      letterSpacing = 6.sp,
      color = TextIvory,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(top = 8.dp)
    )

    Text(
      text = "MARRIAGE BUREAU",
      fontFamily = FontFamily.Serif,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      letterSpacing = 3.5.sp,
      color = GoldPrimary,
      textAlign = TextAlign.Center,
      modifier = Modifier.padding(top = 3.dp)
    )

    JewelDivider(
      modifier = Modifier.padding(vertical = 6.dp),
      width = 195.dp
    )

    Text(
      text = "Nikah Khoobsurat Rishton Ka Aaghaz",
      fontFamily = FontFamily.SansSerif,
      fontWeight = FontWeight.Medium,
      fontSize = 12.5.sp,
      letterSpacing = 0.8.sp,
      color = GoldMuted,
      textAlign = TextAlign.Center
    )

    // Optional Mode Badge / Subtitle pill (with Animated Lens moving directly over search text)
    if (badgeTitle != null) {
      if (badgeTitle.contains("SEARCH", ignoreCase = true)) {
        SearchProfileLensBadge(title = badgeTitle)
      } else {
        Box(
          modifier = Modifier
            .padding(top = 10.dp)
            .border(
              width = 1.dp,
              color = GoldPrimary.copy(alpha = 0.6f),
              shape = RoundedCornerShape(50)
            )
            .background(
              color = RoyalBlack.copy(alpha = 0.8f),
              shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 24.dp, vertical = 5.dp)
        ) {
          Text(
            text = badgeTitle,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 2.sp,
            color = GoldLight,
            textAlign = TextAlign.Center
          )
        }
      }

      if (badgeSubtitle != null) {
        Text(
          text = badgeSubtitle,
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.SemiBold,
          fontSize = 9.sp,
          letterSpacing = 1.2.sp,
          color = TextSand,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
        )
      }
    }
  }
}

@Composable
fun SearchProfileLensBadge(
  title: String,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "searchLensBadge")

  // Lens sweep animation: Middle (0.5f) -> Left (0.08f) -> Right (0.92f) -> Middle (0.5f)
  val lensSweepFraction by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 0.5f,
    animationSpec = infiniteRepeatable(
      animation = keyframes {
        durationMillis = 5200
        0.5f at 0 using FastOutSlowInEasing // Starts in the Middle
        0.08f at 1500 using FastOutSlowInEasing // Moves to Left over "SEARCH"
        0.92f at 3700 using FastOutSlowInEasing // Glides across to Right over "PROFILES"
        0.5f at 5200 using FastOutSlowInEasing // Returns to Center
      },
      repeatMode = RepeatMode.Restart
    ),
    label = "lensSweepFraction"
  )

  val lensBobbing by infiniteTransition.animateFloat(
    initialValue = -1.5f,
    targetValue = 1.5f,
    animationSpec = infiniteRepeatable(
      animation = tween(1300, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "lensBobbing"
  )

  val lensGlowPulse by infiniteTransition.animateFloat(
    initialValue = 0.7f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "lensGlow"
  )

  BoxWithConstraints(
    modifier = modifier
      .padding(top = 10.dp)
      .clip(RoundedCornerShape(50))
      .background(
        brush = Brush.horizontalGradient(
          listOf(
            RoyalBlack.copy(alpha = 0.95f),
            Color(0xFF0F1612).copy(alpha = 0.98f),
            RoyalBlack.copy(alpha = 0.95f)
          )
        ),
        shape = RoundedCornerShape(50)
      )
      .border(
        width = 1.dp,
        brush = Brush.horizontalGradient(
          listOf(
            GoldPrimary.copy(alpha = 0.45f),
            GoldLight.copy(alpha = 0.85f),
            GoldPrimary.copy(alpha = 0.45f)
          )
        ),
        shape = RoundedCornerShape(50)
      )
      .padding(horizontal = 26.dp, vertical = 6.dp)
      .testTag("search_profile_animated_badge"),
    contentAlignment = Alignment.Center
  ) {
    val badgeWidth = maxWidth

    // 1. Base Text: "SEARCH PROFILES"
    Text(
      text = title,
      fontFamily = FontFamily.Serif,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      letterSpacing = 2.sp,
      color = GoldLight,
      textAlign = TextAlign.Center
    )

    // 2. Optical Glass Spotlight that moves with the lens over the text
    Canvas(modifier = Modifier.matchParentSize()) {
      val canvasX = size.width * lensSweepFraction
      val canvasY = size.height * 0.5f

      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            GoldLight.copy(alpha = 0.35f * lensGlowPulse),
            GoldPrimary.copy(alpha = 0.12f * lensGlowPulse),
            Color.Transparent
          ),
          center = Offset(canvasX, canvasY),
          radius = 22.dp.toPx()
        ),
        radius = 22.dp.toPx(),
        center = Offset(canvasX, canvasY)
      )
    }

    // 3. Golden Magnifying Lens Moving Directly Over the Text
    val lensPosX = badgeWidth * lensSweepFraction
    Box(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .offset(
          x = (lensPosX - 17.dp),
          y = lensBobbing.dp
        )
        .size(34.dp),
      contentAlignment = Alignment.Center
    ) {
      // Outer lens halo
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(
            brush = Brush.radialGradient(
              colors = listOf(
                GoldLight.copy(alpha = 0.35f * lensGlowPulse),
                Color.Transparent
              )
            )
          )
      )

      // Metallic Gold Lens Ring & Glass reflection
      Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = 10.5.dp.toPx()
        val centerOffset = Offset(size.width * 0.44f, size.height * 0.44f)

        // Glass reflection arc
        drawCircle(
          brush = Brush.linearGradient(
            colors = listOf(
              Color.White.copy(alpha = 0.35f),
              GoldLight.copy(alpha = 0.15f),
              Color.Transparent
            ),
            start = Offset(centerOffset.x - radius, centerOffset.y - radius),
            end = Offset(centerOffset.x + radius, centerOffset.y + radius)
          ),
          radius = radius,
          center = centerOffset
        )

        // Outer Metallic Gold Ring
        drawCircle(
          color = GoldLight,
          radius = radius,
          center = centerOffset,
          style = Stroke(width = 2.dp.toPx())
        )

        // Inner Shimmer Ring
        drawCircle(
          color = GoldDark,
          radius = radius - 1.2.dp.toPx(),
          center = centerOffset,
          style = Stroke(width = 0.75.dp.toPx())
        )

        // Gold Handle angled down-right
        val handleStart = Offset(centerOffset.x + (radius * 0.7f), centerOffset.y + (radius * 0.7f))
        val handleEnd = Offset(size.width * 0.86f, size.height * 0.86f)
        drawLine(
          brush = Brush.linearGradient(listOf(GoldLight, GoldDark)),
          start = handleStart,
          end = handleEnd,
          strokeWidth = 2.8.dp.toPx(),
          cap = StrokeCap.Round
        )
      }

      // Sparkle Icon on top of Lens
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = GoldLight.copy(alpha = lensGlowPulse),
        modifier = Modifier
          .align(Alignment.TopEnd)
          .size(9.dp)
      )
    }
  }
}
