package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextSand

@Composable
fun HeightFeetInchesPicker(
  feet: String,
  inches: String,
  onFeetChange: (String) -> Unit,
  onInchesChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var feetExpanded by remember { mutableStateOf(false) }
  var inchesExpanded by remember { mutableStateOf(false) }

  val feetOptions = listOf("4", "5", "6", "7")
  val inchesOptions = (0..11).map { it.toString() }

  Column(modifier = modifier.fillMaxWidth()) {
    // Header Row with Height Icon & Label
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 6.dp, start = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Height,
          contentDescription = "Height",
          tint = GoldPrimary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "HEIGHT (FEET & INCHES)",
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TextGold
        )
      }

      // Preview formatted badge like 5'6"
      Box(
        modifier = Modifier
          .border(
            width = 1.dp,
            color = GoldPrimary.copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp)
          )
          .background(RoyalBlack, shape = RoundedCornerShape(8.dp))
          .padding(horizontal = 8.dp, vertical = 2.dp)
      ) {
        Text(
          text = "$feet'$inches\"",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = GoldLight
        )
      }
    }

    // Two side-by-side dropdown fields for Feet and Inches
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // 1. FEET SELECTOR
      Box(
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("dropdown_height_feet")
          .clip(RoundedCornerShape(14.dp))
          .border(
            width = 1.dp,
            color = GoldLight.copy(alpha = 0.5f),
            shape = RoundedCornerShape(14.dp)
          )
          .background(
            color = androidx.compose.ui.graphics.Color(0xFF0F110F)
          )
          .clickable { feetExpanded = true }
          .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "$feet Feet (ft)",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
          )
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Select Feet",
            tint = GoldLight,
            modifier = Modifier.size(18.dp)
          )
        }

        DropdownMenu(
          expanded = feetExpanded,
          onDismissRequest = { feetExpanded = false },
          modifier = Modifier
            .background(SurfaceDark)
            .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        ) {
          feetOptions.forEach { f ->
            DropdownMenuItem(
              text = {
                Text(
                  text = "$f Feet",
                  color = if (f == feet) GoldLight else TextIvory,
                  fontWeight = if (f == feet) FontWeight.Bold else FontWeight.Normal
                )
              },
              onClick = {
                onFeetChange(f)
                feetExpanded = false
              },
              colors = MenuDefaults.itemColors(
                textColor = TextIvory
              )
            )
          }
        }
      }

      // 2. INCHES SELECTOR
      Box(
        modifier = Modifier
          .weight(1f)
          .height(48.dp)
          .testTag("dropdown_height_inches")
          .clip(RoundedCornerShape(14.dp))
          .border(
            width = 1.dp,
            color = GoldLight.copy(alpha = 0.5f),
            shape = RoundedCornerShape(14.dp)
          )
          .background(
            color = androidx.compose.ui.graphics.Color(0xFF0F110F)
          )
          .clickable { inchesExpanded = true }
          .padding(horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "$inches Inches (in)",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
          )
          Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Select Inches",
            tint = GoldLight,
            modifier = Modifier.size(18.dp)
          )
        }

        DropdownMenu(
          expanded = inchesExpanded,
          onDismissRequest = { inchesExpanded = false },
          modifier = Modifier
            .background(SurfaceDark)
            .border(1.dp, GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        ) {
          inchesOptions.forEach { inc ->
            DropdownMenuItem(
              text = {
                Text(
                  text = "$inc Inches",
                  color = if (inc == inches) GoldLight else TextIvory,
                  fontWeight = if (inc == inches) FontWeight.Bold else FontWeight.Normal
                )
              },
              onClick = {
                onInchesChange(inc)
                inchesExpanded = false
              },
              colors = MenuDefaults.itemColors(
                textColor = TextIvory
              )
            )
          }
        }
      }
    }
  }
}
