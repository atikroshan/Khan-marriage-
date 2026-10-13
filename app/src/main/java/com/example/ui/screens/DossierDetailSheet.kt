package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.AppDatabase
import com.example.data.CandidateDossier
import com.example.ui.components.JewelDivider
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextSand
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DossierDetailSheet(
  dossier: CandidateDossier?,
  onDismiss: () -> Unit,
  onSendWhatsApp: (CandidateDossier) -> Unit
) {
  if (dossier == null) return

  val context = LocalContext.current
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val isDulhan = dossier.gender.equals("Dulhan", ignoreCase = true)
  val defaultImg = if (isDulhan) AppDatabase.DULHAN_IMAGE_URL else AppDatabase.DULHA_IMAGE_URL

  val img1 = dossier.frontPortraitUrl.ifBlank { dossier.fullLengthUrl.ifBlank { defaultImg } }
  val img2 = dossier.fullLengthUrl.ifBlank { dossier.frontPortraitUrl.ifBlank { defaultImg } }

  val photoList = listOf(
    "Portrait Photo" to img1,
    "Full Length Photo" to img2
  )

  var showPhotoPopup by remember { mutableStateOf(false) }
  var selectedPhotoIndex by remember { mutableIntStateOf(0) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFF0F110F),
    contentColor = TextIvory
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 32.dp, start = 20.dp, end = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Header: Code, Share and Close
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = dossier.dossierCode,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = GoldLight
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { onSendWhatsApp(dossier) },
            modifier = Modifier.testTag("send_whatsapp_dossier_button")
          ) {
            Icon(
              imageVector = Icons.Default.Send, // Or use a WhatsApp icon if available
              contentDescription = "Send via WhatsApp",
              tint = Color(0xFF25D366) // WhatsApp green
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_dossier_detail_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = TextSand
            )
          }
        }
      }

      // Photos Gallery (Portrait + Full Length)
      // Tapping on either photo opens full popup gallery with horizontal sliding
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Portrait Box
        Box(
          modifier = Modifier
            .weight(1f)
            .height(175.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldPrimary.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable {
              selectedPhotoIndex = 0
              showPhotoPopup = true
            }
            .testTag("photo_portrait_thumbnail")
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(img1)
              .crossfade(true)
              .build(),
            contentDescription = "Front Portrait Photo",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )

          // Portrait badge
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(RoyalBlack.copy(alpha = 0.75f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("Portrait", fontSize = 10.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
          }

          // Tap to expand indicator
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(6.dp)
              .size(24.dp)
              .background(RoyalBlack.copy(alpha = 0.65f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Fullscreen,
              contentDescription = "Expand Photo",
              tint = GoldLight,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        // Full Length Box
        Box(
          modifier = Modifier
            .weight(1f)
            .height(175.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldPrimary.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .clickable {
              selectedPhotoIndex = 1
              showPhotoPopup = true
            }
            .testTag("photo_full_length_thumbnail")
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(img2)
              .crossfade(true)
              .build(),
            contentDescription = "Full Length Photo",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )

          // Full Length badge
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(RoyalBlack.copy(alpha = 0.75f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("Full Length", fontSize = 10.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
          }

          // Tap to expand indicator
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(6.dp)
              .size(24.dp)
              .background(RoyalBlack.copy(alpha = 0.65f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Fullscreen,
              contentDescription = "Expand Photo",
              tint = GoldLight,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Tap hint
      Text(
        text = "Tap photo to view fullscreen & slide",
        fontSize = 11.sp,
        color = TextSand.copy(alpha = 0.65f),
        modifier = Modifier.padding(top = 6.dp)
      )

      // Candidate Name & Title
      Text(
        text = dossier.candidateName,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = TextIvory,
        modifier = Modifier.padding(top = 10.dp)
      )

      Text(
        text = "${dossier.gender} • ${dossier.age} Yrs • ${dossier.height}",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = GoldPrimary,
        modifier = Modifier.padding(top = 2.dp)
      )

      JewelDivider(modifier = Modifier.padding(vertical = 12.dp))

      // Detailed Info Grid (Without Caste & Sect as requested)
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        DetailRow(Icons.Default.LocationOn, "Location", dossier.city)
        DetailRow(Icons.Default.Favorite, "Marital Status", dossier.maritalStatus)
        DetailRow(Icons.Default.Business, "Occupation", dossier.occupation)
        DetailRow(Icons.Default.School, "Education", dossier.education)
        DetailRow(Icons.Default.Palette, "Complexion", dossier.complexion)
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  // Fullscreen Photo Popup Dialog with slide/swipe gesture and close button
  if (showPhotoPopup) {
    PhotoViewerDialog(
      photos = photoList,
      initialIndex = selectedPhotoIndex,
      candidateName = dossier.candidateName,
      onDismiss = { showPhotoPopup = false }
    )
  }
}

@Composable
private fun PhotoViewerDialog(
  photos: List<Pair<String, String>>,
  initialIndex: Int,
  candidateName: String,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState(
    initialPage = initialIndex.coerceIn(0, (photos.size - 1).coerceAtLeast(0))
  ) {
    photos.size
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnBackPress = true,
      dismissOnClickOutside = true
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xF0050806))
        .testTag("photo_viewer_dialog")
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // Top Bar: Candidate name, photo title and Close Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = candidateName,
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = TextIvory
            )
            Text(
              text = "${photos.getOrNull(pagerState.currentPage)?.first ?: "Photo"} • ${pagerState.currentPage + 1}/${photos.size}",
              fontSize = 12.sp,
              color = GoldLight,
              fontWeight = FontWeight.Medium
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(42.dp)
              .background(Color(0xFF1B241D), CircleShape)
              .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape)
              .testTag("close_photo_viewer_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Photo Viewer",
              tint = GoldLight,
              modifier = Modifier.size(22.dp)
            )
          }
        }

        // Pager Content: Slide between photos
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
          ) { page ->
            val photoInfo = photos[page]
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              AsyncImage(
                model = ImageRequest.Builder(context)
                  .data(photoInfo.second)
                  .crossfade(true)
                  .build(),
                contentDescription = photoInfo.first,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(16.dp))
              )
            }
          }

          // Left navigation arrow button (slide to previous photo)
          if (pagerState.currentPage > 0) {
            IconButton(
              onClick = {
                coroutineScope.launch {
                  pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
              },
              modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
                .size(46.dp)
                .background(RoyalBlack.copy(alpha = 0.75f), CircleShape)
                .border(1.dp, GoldPrimary.copy(alpha = 0.45f), CircleShape)
                .testTag("photo_nav_prev")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous Photo",
                tint = GoldLight
              )
            }
          }

          // Right navigation arrow button (slide to next photo)
          if (pagerState.currentPage < photos.size - 1) {
            IconButton(
              onClick = {
                coroutineScope.launch {
                  pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
              },
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp)
                .size(46.dp)
                .background(RoyalBlack.copy(alpha = 0.75f), CircleShape)
                .border(1.dp, GoldPrimary.copy(alpha = 0.45f), CircleShape)
                .testTag("photo_nav_next")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next Photo",
                tint = GoldLight
              )
            }
          }
        }

        // Bottom Controls: Page indicators and slide hint
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp, top = 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Photo selector pills
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            photos.forEachIndexed { index, pair ->
              val isSelected = pagerState.currentPage == index
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(20.dp))
                  .background(if (isSelected) GoldPrimary else Color(0xFF1D261F))
                  .border(
                    1.dp,
                    if (isSelected) GoldLight else GoldPrimary.copy(alpha = 0.3f),
                    RoundedCornerShape(20.dp)
                  )
                  .clickable {
                    coroutineScope.launch {
                      pagerState.animateScrollToPage(index)
                    }
                  }
                  .padding(horizontal = 14.dp, vertical = 7.dp)
              ) {
                Text(
                  text = pair.first,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) TextDark else TextIvory
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Swipe left / right or tap arrows to slide",
            fontSize = 11.sp,
            color = TextSand.copy(alpha = 0.65f)
          )
        }
      }
    }
  }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = GoldPrimary,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(10.dp))
    Text(
      text = "$label:",
      fontFamily = FontFamily.SansSerif,
      fontWeight = FontWeight.SemiBold,
      fontSize = 12.sp,
      color = TextSand,
      modifier = Modifier.width(100.dp)
    )
    Text(
      text = value,
      fontFamily = FontFamily.SansSerif,
      fontWeight = FontWeight.Normal,
      fontSize = 13.sp,
      color = TextIvory
    )
  }
}
