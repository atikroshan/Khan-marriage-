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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
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
      // 1. New Registration Card (Matching Submit Button Horizontal Gold Gradient & Black Text)
      Box(
        modifier = Modifier
          .width(200.dp)
          .height(160.dp)
          .testTag("new_registration_card")
          .clip(RoundedCornerShape(22.dp))
          .border(
            width = 1.5.dp,
            color = GoldLight.copy(alpha = glowAlpha),
            shape = RoundedCornerShape(22.dp)
          )
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(GoldLight, GoldMedium, GoldDark)
            )
          )
          .clickable { onNavigateToNewRegistration() }
          .padding(14.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Top Icon Circle
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color(0xFF141512))
              .border(1.dp, Color(0xFF141512).copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.PersonAdd,
              contentDescription = "New Registration",
              tint = GoldLight,
              modifier = Modifier.size(24.dp)
            )
          }

          // Text Content (Same text color as submit button: Color(0xFF141512))
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "New Registration",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF141512),
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "Establish Candidate Dossier",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              color = Color(0xFF141512).copy(alpha = 0.85f)
            )
          }

          // Bottom Arrow Circle
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Color(0xFF141512)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Proceed to registration",
              tint = GoldLight,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }

      // Middle Jewel Divider between cards
      JewelDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        width = 180.dp
      )

      // 2. Search Profiles Card (Dark Emerald Gradient)
      Box(
        modifier = Modifier
          .width(200.dp)
          .height(160.dp)
          .testTag("search_profiles_card")
          .clip(RoundedCornerShape(22.dp))
          .border(
            width = 1.5.dp,
            color = GoldPrimary.copy(alpha = glowAlpha),
            shape = RoundedCornerShape(22.dp)
          )
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(Color(0xFF0E271C), Color(0xFF071710))
            )
          )
          .clickable { onNavigateToSearchProfiles() }
          .padding(14.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          // Top Icon Circle
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color(0xFF05130D))
              .border(1.dp, GoldPrimary.copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search Profiles",
              tint = GoldLight,
              modifier = Modifier.size(24.dp)
            )
          }

          // Text Content
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Search Profiles",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFFFAFAF6),
              letterSpacing = 0.3.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
              text = "Explore Verified Matches",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Medium,
              fontSize = 10.sp,
              color = TextSand
            )
          }

          // Bottom Filter Tune Circle
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Color(0xFF05130D))
              .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Filter profiles",
              tint = GoldLight,
              modifier = Modifier.size(14.dp)
            )
          }
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
