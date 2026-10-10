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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.theme.SurfaceDark
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

    // Middle: DULHA & DULHAN Sections with Bouncing Circle Images and Cards, shifted higher up
    val infiniteTransition = rememberInfiniteTransition(label = "bouncingCircles")
    val bounceOffset by infiniteTransition.animateFloat(
      initialValue = -3.5f,
      targetValue = 3.5f,
      animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "bounceOffset"
    )

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // 1. DULHA SECTION: Overlapping Bouncing Avatar + Rounded Corner Card
      Box(
        modifier = Modifier
          .width(215.dp)
          .height(176.dp)
          .testTag("select_dulha_button")
          .clickable { onSelectGender("Dulha") },
        contentAlignment = Alignment.TopCenter
      ) {
        // The Card: Aligned to BottomCenter (height 144dp, rounded corners 26dp)
        Box(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .width(215.dp)
            .height(144.dp)
            .clip(RoundedCornerShape(26.dp))
            .border(
              width = 1.dp,
              color = GoldLight.copy(alpha = 0.85f),
              shape = RoundedCornerShape(26.dp)
            )
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  Color(0xFFF7DE9E),
                  Color(0xFFE5B962),
                  Color(0xFFD49B3E)
                )
              )
            )
            .padding(top = 36.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "DULHA",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color(0xFF141512),
                letterSpacing = 1.5.sp
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "( GROOM )",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
                color = Color(0xFF423007),
                letterSpacing = 1.sp
              )
            }

            // Circular action arrow at bottom
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF141512))
                .border(1.dp, GoldLight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Dulha",
                tint = GoldLight,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }

        // Circular Avatar with gentle bouncing animation (Sits centered on top edge of the card)
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .graphicsLayer { translationY = bounceOffset }
            .size(64.dp)
            .clip(CircleShape)
            .border(2.dp, GoldLight, CircleShape)
            .border(4.dp, GoldLight.copy(alpha = 0.35f), CircleShape)
            .background(SurfaceDark),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(AppDatabase.DULHA_IMAGE_URL)
              .crossfade(true)
              .build(),
            contentDescription = "Dulha Groom Avatar",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )
        }
      }

      // Middle Jewel Divider between Dulha and Dulhan
      JewelDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        width = 180.dp
      )

      // 2. DULHAN SECTION: Overlapping Bouncing Avatar + Rounded Corner Card
      Box(
        modifier = Modifier
          .width(215.dp)
          .height(176.dp)
          .testTag("select_dulhan_button")
          .clickable { onSelectGender("Dulhan") },
        contentAlignment = Alignment.TopCenter
      ) {
        // The Card: Aligned to BottomCenter (height 144dp, rounded corners 26dp)
        Box(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .width(215.dp)
            .height(144.dp)
            .clip(RoundedCornerShape(26.dp))
            .border(
              width = 1.dp,
              color = RoseGoldBorder.copy(alpha = 0.85f),
              shape = RoundedCornerShape(26.dp)
            )
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  Color(0xFFF7C3B7),
                  Color(0xFFE59C8C),
                  Color(0xFFC57568)
                )
              )
            )
            .padding(top = 36.dp, bottom = 12.dp, start = 14.dp, end = 14.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "DULHAN",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color(0xFF2E1014),
                letterSpacing = 1.5.sp
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "( BRIDE )",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                fontSize = 11.5.sp,
                color = Color(0xFF5A2028),
                letterSpacing = 1.sp
              )
            }

            // Circular action arrow at bottom
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1012))
                .border(1.dp, RoseGoldLight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Select Dulhan",
                tint = RoseGoldLight,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }

        // Circular Avatar with gentle bouncing animation (Sits centered on top edge of the card)
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .graphicsLayer { translationY = -bounceOffset }
            .size(64.dp)
            .clip(CircleShape)
            .border(2.dp, RoseGoldLight, CircleShape)
            .border(4.dp, RoseGoldLight.copy(alpha = 0.35f), CircleShape)
            .background(SurfaceDark),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(AppDatabase.DULHAN_IMAGE_URL)
              .crossfade(true)
              .build(),
            contentDescription = "Dulhan Bride Avatar",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
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
