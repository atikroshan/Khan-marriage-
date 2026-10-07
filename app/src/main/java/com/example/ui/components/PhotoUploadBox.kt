package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSand

@Composable
fun PhotoUploadSection(
  image1Uri: String?,
  image2Uri: String?,
  onImage1Selected: (String?) -> Unit,
  onImage2Selected: (String?) -> Unit,
  modifier: Modifier = Modifier
) {
  val launcher1 = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      onImage1Selected(uri.toString())
    }
  }

  val launcher2 = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      onImage2Selected(uri.toString())
    }
  }

  Column(modifier = modifier.fillMaxWidth()) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp, start = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.PhotoCamera,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "UPLOAD PHOTOS / IMAGES",
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp,
          color = TextGold
        )
      }

      Text(
        text = "(MAX 2)",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        color = TextMuted
      )
    }

    // Two upload boxes side by side
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      PhotoUploadCard(
        title = "Image 1",
        subtitle = "Front Portrait",
        icon = Icons.Default.Upload,
        imageUrl = image1Uri,
        testTag = "upload_image_1",
        onClick = {
          launcher1.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
          )
        },
        onClear = { onImage1Selected(null) },
        modifier = Modifier.weight(1f)
      )

      PhotoUploadCard(
        title = "Image 2",
        subtitle = "Full Length",
        icon = Icons.Default.Image,
        imageUrl = image2Uri,
        testTag = "upload_image_2",
        onClick = {
          launcher2.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
          )
        },
        onClear = { onImage2Selected(null) },
        modifier = Modifier.weight(1f)
      )
    }
  }
}

@Composable
fun PhotoUploadCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  imageUrl: String?,
  testTag: String,
  onClick: () -> Unit,
  onClear: () -> Unit,
  modifier: Modifier = Modifier
) {
  val hasImage = !imageUrl.isNullOrBlank()

  Box(
    modifier = modifier
      .height(104.dp)
      .testTag(testTag)
      .clip(RoundedCornerShape(16.dp))
      .border(
        width = 1.dp,
        color = if (hasImage) GoldPrimary else EmeraldBorder.copy(alpha = 0.8f),
        shape = RoundedCornerShape(16.dp)
      )
      .background(
        color = Color(0xFF09140E),
        shape = RoundedCornerShape(16.dp)
      )
      .clickable { onClick() }
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    if (hasImage) {
      Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
          contentDescription = title,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
        )

        // Clear button overlay
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(2.dp)
            .size(24.dp)
            .clip(CircleShape)
            .background(RoyalBlack.copy(alpha = 0.75f))
            .clickable { onClear() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove photo",
            tint = TextIvory,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    } else {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        // Circle icon button
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape)
            .background(RoyalBlack.copy(alpha = 0.6f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GoldLight,
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = title,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = TextIvory
        )

        Text(
          text = subtitle,
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Normal,
          fontSize = 10.sp,
          color = TextSand.copy(alpha = 0.8f)
        )
      }
    }
  }
}
