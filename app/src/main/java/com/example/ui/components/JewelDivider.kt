package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RubyJewel

@Composable
fun JewelDivider(
  modifier: Modifier = Modifier,
  width: Dp = 195.dp
) {
  Row(
    modifier = modifier.width(width),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    // Left tapering line
    Box(
      modifier = Modifier
        .weight(1f)
        .height(1.2.dp)
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, GoldPrimary)
          )
        )
    )

    // Center jewel diamonds: Ruby, Gold, Ruby
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(5.dp),
      modifier = Modifier.padding(horizontal = 7.dp)
    ) {
      Box(
        modifier = Modifier
          .size(5.5.dp)
          .rotate(45f)
          .background(RubyJewel)
      )
      Box(
        modifier = Modifier
          .size(8.dp)
          .rotate(45f)
          .background(GoldPrimary)
      )
      Box(
        modifier = Modifier
          .size(5.5.dp)
          .rotate(45f)
          .background(RubyJewel)
      )
    }

    // Right tapering line
    Box(
      modifier = Modifier
        .weight(1f)
        .height(1.2.dp)
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(GoldPrimary, Color.Transparent)
          )
        )
    )
  }
}
