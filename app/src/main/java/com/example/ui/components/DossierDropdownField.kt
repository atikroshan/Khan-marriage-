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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextSand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DossierDropdownField(
  label: String,
  icon: ImageVector,
  selectedValue: String,
  options: List<String>,
  placeholder: String,
  testTag: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showPicker by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  Column(modifier = modifier.fillMaxWidth()) {
    // Header Row: Icon + Label
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = GoldPrimary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = label.uppercase(),
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        color = TextGold
      )
    }

    // Input Field Box (Black Background with White Text)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag(testTag)
        .clip(RoundedCornerShape(14.dp))
        .border(
          width = 1.dp,
          color = GoldLight.copy(alpha = 0.5f),
          shape = RoundedCornerShape(14.dp)
        )
        .background(
          color = androidx.compose.ui.graphics.Color(0xFF0F110F)
        )
        .clickable { showPicker = true }
        .padding(horizontal = 14.dp),
      contentAlignment = Alignment.CenterStart
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        val hasValue = selectedValue.isNotBlank() && selectedValue != placeholder
        Text(
          text = if (hasValue) selectedValue else placeholder,
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Medium,
          fontSize = 13.5.sp,
          color = if (hasValue) androidx.compose.ui.graphics.Color(0xFFFFFFFF) else androidx.compose.ui.graphics.Color(0xFF9E9E9E),
          modifier = Modifier.weight(1f)
        )

        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "Expand dropdown",
          tint = GoldLight,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }

  // Selection BottomSheet
  if (showPicker) {
    ModalBottomSheet(
      onDismissRequest = { showPicker = false },
      sheetState = sheetState,
      containerColor = SurfaceDark,
      contentColor = TextIvory
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 32.dp, start = 20.dp, end = 20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = GoldPrimary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Select $label",
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = TextIvory
            )
          }

          TextButton(onClick = { showPicker = false }) {
            Text("Done", color = GoldLight, fontWeight = FontWeight.Bold)
          }
        }

        HorizontalDivider(
          color = GoldPrimary.copy(alpha = 0.2f),
          modifier = Modifier.padding(vertical = 12.dp)
        )

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 360.dp)
        ) {
          items(options) { item ->
            val isSelected = item == selectedValue
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onValueChange(item)
                  showPicker = false
                }
                .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = item,
                  fontFamily = FontFamily.SansSerif,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 14.sp,
                  color = if (isSelected) GoldLight else TextIvory
                )
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
