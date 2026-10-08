package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.ui.components.JewelDivider
import com.example.ui.components.KhanHeader
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMedium
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoseGoldBorder
import com.example.ui.theme.RoseGoldDark
import com.example.ui.theme.RoseGoldLight
import com.example.ui.theme.RoseGoldMedium
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextRoseDark
import com.example.ui.theme.TextRoseMuted
import com.example.ui.theme.TextSand

@Composable
fun GenderSelectionScreen(
  modeTitle: String? = null,
  onSelectGender: (gender: String) -> Unit,
  onBack: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  if (onBack != null) {
    BackHandler { onBack() }
  }

  // Smooth continuous slow bouncing animation for cards and overlapping avatars
  val infiniteTransition = rememberInfiniteTransition(label = "CardBounceTransition")
  val bounceOffsetDulha by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -7f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "DulhaBounce"
  )
  val bounceOffsetDulhan by infiniteTransition.animateFloat(
    initialValue = -5f,
    targetValue = 2f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "DulhanBounce"
  )

  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top Header with Back Button and bold all caps mode title below punchline (Edge-to-edge full width)
    KhanHeader(
      badgeTitle = modeTitle,
      onBack = onBack,
      modifier = Modifier.fillMaxWidth()
    )

    // Middle: DULHA & DULHAN Cards centered with clean equal padding and divider separation
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // 1. DULHA (GROOM) CARD (Yellow Gold Gradient with slow smooth bounce animation)
      Box(
        modifier = Modifier
          .offset(y = bounceOffsetDulha.dp)
          .width(185.dp)
          .height(168.dp)
          .testTag("select_dulha_card")
          .clickable { onSelectGender("Dulha") },
        contentAlignment = Alignment.BottomCenter
      ) {
        // Background Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(
              width = 1.dp,
              color = GoldLight.copy(alpha = 0.6f),
              shape = RoundedCornerShape(22.dp)
            )
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(GoldLight, GoldMedium, GoldDark)
              )
            )
            .padding(top = 38.dp, bottom = 10.dp, start = 12.dp, end = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            // Text: DULHA (GROOM) in all caps
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "DULHA",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF141512),
                letterSpacing = 1.sp
              )
              Spacer(modifier = Modifier.height(1.dp))
              Text(
                text = "(GROOM)",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = Color(0xFF523D0A),
                letterSpacing = 0.8.sp
              )
            }

            // Arrow button
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.85f))
                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Groom",
                tint = GoldLight,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }

        // Floating Overlapping Portrait Avatar
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 0.dp)
            .size(62.dp)
            .clip(CircleShape)
            .border(2.5.dp, GoldLight, CircleShape)
            .border(4.5.dp, GoldPrimary.copy(alpha = 0.35f), CircleShape)
            .background(Color(0xFF1A1C19))
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(AppDatabase.DULHA_IMAGE_URL)
              .crossfade(true)
              .build(),
            contentDescription = "Dulha Portrait",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }
      }

      // Middle Jewel Divider between cards (Equal top and bottom safe padding so it never touches cards)
      JewelDivider(
        modifier = Modifier.padding(vertical = 24.dp),
        width = 195.dp
      )

      // 2. DULHAN (BRIDE) CARD (Rose Gold Gradient with alternating slow smooth bounce animation)
      Box(
        modifier = Modifier
          .offset(y = bounceOffsetDulhan.dp)
          .width(185.dp)
          .height(168.dp)
          .testTag("select_dulhan_card")
          .clickable { onSelectGender("Dulhan") },
        contentAlignment = Alignment.BottomCenter
      ) {
        // Background Card in Rose Golden Gradient
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(
              width = 1.dp,
              color = RoseGoldBorder.copy(alpha = 0.7f),
              shape = RoundedCornerShape(22.dp)
            )
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(RoseGoldLight, RoseGoldMedium, RoseGoldDark)
              )
            )
            .padding(top = 38.dp, bottom = 10.dp, start = 12.dp, end = 12.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            // Text: DULHAN (BRIDE) in all caps
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "DULHAN",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextRoseDark,
                letterSpacing = 1.sp
              )
              Spacer(modifier = Modifier.height(1.dp))
              Text(
                text = "(BRIDE)",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextRoseMuted,
                letterSpacing = 0.8.sp
              )
            }

            // Arrow button with black background and golden arrow
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.85f))
                .border(1.dp, GoldPrimary.copy(alpha = 0.45f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Bride",
                tint = GoldLight,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }

        // Floating Overlapping Portrait Avatar
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 0.dp)
            .size(62.dp)
            .clip(CircleShape)
            .border(2.5.dp, RoseGoldLight, CircleShape)
            .border(4.5.dp, RoseGoldBorder.copy(alpha = 0.4f), CircleShape)
            .background(Color(0xFF1A1C19))
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(AppDatabase.DULHAN_IMAGE_URL)
              .crossfade(true)
              .build(),
            contentDescription = "Dulhan Portrait",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }

    // Bottom Footer
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(bottom = 12.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Powered by ",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        color = TextMuted
      )
      Text(
        text = "@tek",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = GoldPrimary
      )
      Text(
        text = " • v${BuildConfig.VERSION_NAME}",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 10.5.sp,
        color = TextMuted
      )
    }
  }
}
