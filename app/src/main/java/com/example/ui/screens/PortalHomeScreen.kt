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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.components.JewelDivider
import com.example.ui.components.KhanHeader
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMedium
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSand

@Composable
fun PortalHomeScreen(
  onNavigateToNewRegistration: () -> Unit,
  onNavigateToSearchProfiles: () -> Unit,
  onBack: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  if (onBack != null) {
    BackHandler { onBack() }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "buttonGlow")
  val glowAlpha by infiniteTransition.animateFloat(
    initialValue = 0.35f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glowAlpha"
  )

  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top Section: Header with Back button (Edge-to-edge full width)
    KhanHeader(
      onBack = onBack,
      modifier = Modifier.fillMaxWidth()
    )

    // Middle Section: Two Hero Action Cards (New Registration on top, Search Profiles below)
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // 1. New Registration Card (Square Shape size 140dp x 140dp, 1:1 Aspect Ratio)
      Box(
        modifier = Modifier
          .size(140.dp)
          .testTag("new_registration_card")
          .clip(RoundedCornerShape(4.dp))
          .border(
            width = 1.5.dp,
            color = GoldLight.copy(alpha = glowAlpha),
            shape = RoundedCornerShape(4.dp)
          )
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(GoldLight, GoldMedium, GoldDark)
            )
          )
          .clickable { onNavigateToNewRegistration() }
          .padding(horizontal = 8.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          // Top Icon Box
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF141512))
              .border(1.dp, Color(0xFF141512).copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PersonAdd,
              contentDescription = "New Registration",
              tint = GoldLight,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Text Content
          Text(
            text = "NEW\nREGISTRATION",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFF141512),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Create Dossier",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = Color(0xFF141512).copy(alpha = 0.85f),
            textAlign = TextAlign.Center
          )
        }
      }

      // Middle Jewel Divider between cards (Equal vertical padding)
      JewelDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        width = 150.dp
      )

      // 2. Search Profiles Card (Square Shape size 140dp x 140dp, 1:1 Aspect Ratio)
      Box(
        modifier = Modifier
          .size(140.dp)
          .testTag("search_profiles_card")
          .clip(RoundedCornerShape(4.dp))
          .border(
            width = 1.5.dp,
            color = GoldPrimary.copy(alpha = glowAlpha),
            shape = RoundedCornerShape(4.dp)
          )
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(Color(0xFF0E271C), Color(0xFF071710))
            )
          )
          .clickable { onNavigateToSearchProfiles() }
          .padding(horizontal = 8.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          // Top Icon Box
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF05130D))
              .border(1.dp, GoldPrimary.copy(alpha = 0.45f), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search Profiles",
              tint = GoldLight,
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          // Text Content
          Text(
            text = "SEARCH\nPROFILES",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFFFAFAF6),
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Explore Matches",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 9.sp,
            color = TextSand,
            textAlign = TextAlign.Center
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
