package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.CandidateDossier
import com.example.ui.components.JewelDivider
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMedium
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DossierDetailSheet(
  dossier: CandidateDossier?,
  onDismiss: () -> Unit
) {
  if (dossier == null) return

  val context = LocalContext.current
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
      // Header: Code and Close
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

        IconButton(onClick = onDismiss) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = TextSand
          )
        }
      }

      // Photos Gallery (Portrait + Full Length)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        val img1 = dossier.frontPortraitUrl.ifBlank { dossier.fullLengthUrl }
        val img2 = dossier.fullLengthUrl.ifBlank { dossier.frontPortraitUrl }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(img1)
              .crossfade(true)
              .build(),
            contentDescription = "Front Portrait",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(RoyalBlack.copy(alpha = 0.7f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("Portrait", fontSize = 10.sp, color = GoldLight)
          }
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(170.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(img2)
              .crossfade(true)
              .build(),
            contentDescription = "Full Length",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .background(RoyalBlack.copy(alpha = 0.7f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("Full Length", fontSize = 10.sp, color = GoldLight)
          }
        }
      }

      // Candidate Name & Title
      Text(
        text = dossier.candidateName,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = TextIvory,
        modifier = Modifier.padding(top = 14.dp)
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

      // Detailed Info Grid
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        DetailRow(Icons.Default.LocationOn, "Location", dossier.city)
        DetailRow(Icons.Default.Favorite, "Marital Status", dossier.maritalStatus)
        DetailRow(Icons.Default.Business, "Occupation", dossier.occupation)
        DetailRow(Icons.Default.School, "Education", dossier.education)
        DetailRow(Icons.Default.Palette, "Complexion", dossier.complexion)
        DetailRow(Icons.Default.Person, "Caste & Sect", dossier.casteSect)
      }

      // Family & Background Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 14.dp)
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFF09140E))
          .border(1.dp, EmeraldBorder, RoundedCornerShape(14.dp))
          .padding(14.dp)
      ) {
        Column {
          Text(
            text = "FAMILY & VERIFICATION NOTES",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = GoldLight
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = dossier.familyDetails,
            fontSize = 12.sp,
            color = TextIvory,
            lineHeight = 17.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Express Interest / WhatsApp
        Button(
          onClick = {
            val message = "Hello Khan Marriage Bureau, I am interested in inquiring about candidate dossier ID: ${dossier.dossierCode} (${dossier.candidateName})."
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=923001234567&text=${Uri.encode(message)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
              context.startActivity(intent)
            } catch (e: Exception) {
              Toast.makeText(context, "Inquiry sent for ${dossier.dossierCode}", Toast.LENGTH_SHORT).show()
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = TextDark
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("inquire_dossier_button")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Inquire Bureau", fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }

        // Share Dossier Summary
        Button(
          onClick = {
            val shareText = "Khan Marriage Bureau Dossier:\nCandidate ID: ${dossier.dossierCode}\nGender: ${dossier.gender}\nAge: ${dossier.age} Yrs\nCity: ${dossier.city}\nEducation: ${dossier.education}\nOccupation: ${dossier.occupation}"
            val sendIntent = Intent().apply {
              action = Intent.ACTION_SEND
              putExtra(Intent.EXTRA_TEXT, shareText)
              type = "text/plain"
            }
            context.startActivity(Intent.createChooser(sendIntent, "Share Dossier Summary"))
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1B261D),
            contentColor = GoldLight
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.height(48.dp)
        ) {
          Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(18.dp))
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
