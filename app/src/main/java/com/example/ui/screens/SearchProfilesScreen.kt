package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.AppDatabase
import com.example.data.CandidateDossier
import com.example.ui.components.AnimatedSearchProfileCard
import com.example.ui.components.JewelDivider
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldMedium
import com.example.ui.theme.GoldMuted
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RoyalBlack
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextIvory
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSand

@Composable
fun SearchProfilesScreen(
  dossiers: List<CandidateDossier>,
  initialGender: String = "All",
  onNavigateToNewRegistration: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }

  var selectedGenderTab by remember(initialGender) { mutableStateOf(initialGender) }
  var searchQuery by remember { mutableStateOf("") }
  var selectedCityFilter by remember { mutableStateOf<String?>(null) }
  var selectedDossierForDetail by remember { mutableStateOf<CandidateDossier?>(null) }

  // Subtle pulsing animation for search lens and active search indicators
  val infiniteTransition = rememberInfiniteTransition(label = "searchPulse")
  val searchIconScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "searchScale"
  )
  val searchGlowAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 0.95f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "searchGlow"
  )

  val filteredList = dossiers.filter { dossier ->
    val matchesGender = when (selectedGenderTab) {
      "Dulhan" -> dossier.gender.equals("Dulhan", ignoreCase = true)
      "Dulha" -> dossier.gender.equals("Dulha", ignoreCase = true)
      else -> true
    }

    val matchesCity = if (selectedCityFilter == null) true else dossier.city.contains(selectedCityFilter!!, ignoreCase = true)

    val matchesQuery = if (searchQuery.isBlank()) true else {
      dossier.candidateName.contains(searchQuery, ignoreCase = true) ||
        dossier.dossierCode.contains(searchQuery, ignoreCase = true) ||
        dossier.city.contains(searchQuery, ignoreCase = true) ||
        dossier.occupation.contains(searchQuery, ignoreCase = true) ||
        dossier.education.contains(searchQuery, ignoreCase = true)
    }

    matchesGender && matchesCity && matchesQuery
  }

  val quickCities = listOf(
    "All",
    "Solapur",
    "Mumbai",
    "Pune",
    "Akkalkot",
    "Pandharpur",
    "Sangli",
    "Satara",
    "Osmanabad",
    "Gulbarga",
    "Hyderabad"
  )

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Bar with Prominent Back Button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Prominent Luxury Back Button
        Box(
          modifier = Modifier
            .height(38.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF141714))
            .border(1.dp, GoldPrimary, RoundedCornerShape(50))
            .clickable { onBack() }
            .padding(horizontal = 12.dp)
            .testTag("search_back_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = GoldLight,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Back",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = GoldLight
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
          modifier = Modifier.weight(1f),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "KHAN MARRIAGE BUREAU",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.5.sp,
            color = GoldLight
          )
          Text(
            text = "VERIFIED MATCHES",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            color = TextSand
          )
        }

        Spacer(modifier = Modifier.width(60.dp))
      }

      // Animated "Search Profile" Card with lens moving from Right to Left (pauses 5s on right, reveals text)
      AnimatedSearchProfileCard(
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(top = 4.dp, bottom = 10.dp),
        onClick = onNavigateToNewRegistration
      )

      // Search Bar with Small Animation Effect
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by code, name, city, or profession...", color = TextSand.copy(alpha = 0.6f), fontSize = 12.sp) },
        leadingIcon = {
          Box(
            modifier = Modifier
              .padding(start = 6.dp)
              .size(32.dp)
              .clip(CircleShape)
              .background(GoldPrimary.copy(alpha = if (searchQuery.isNotBlank()) searchGlowAlpha * 0.25f else 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search icon",
              tint = if (searchQuery.isNotBlank()) GoldLight else GoldPrimary,
              modifier = Modifier
                .size(18.dp)
                .graphicsLayer(scaleX = searchIconScale, scaleY = searchIconScale)
            )
          }
        },
        trailingIcon = {
          if (searchQuery.isNotBlank()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSand, modifier = Modifier.size(16.dp))
            }
          }
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = GoldPrimary.copy(alpha = 0.35f),
          focusedTextColor = TextIvory,
          unfocusedTextColor = TextIvory,
          focusedContainerColor = SurfaceDark,
          unfocusedContainerColor = SurfaceDark
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .testTag("search_input_field")
      )

      // Animated Match Status Counter Pill
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(CircleShape)
              .background(GoldPrimary.copy(alpha = searchGlowAlpha))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (searchQuery.isNotBlank()) "Searching \"$searchQuery\"" else "Exploring ${if (selectedGenderTab == "All") "All" else selectedGenderTab} Matches",
            fontSize = 11.sp,
            color = GoldMuted,
            fontWeight = FontWeight.Medium
          )
        }

        AnimatedContent(
          targetState = filteredList.size,
          label = "CountAnimation"
        ) { count ->
          Text(
            text = "$count Profiles",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GoldLight
          )
        }
      }

      // Gender Category Tabs: "All", "Dulhan (Brides)", "Dulha (Grooms)"
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        GenderFilterPill(
          title = "All Profiles (${dossiers.size})",
          isSelected = selectedGenderTab == "All",
          onClick = { selectedGenderTab = "All" },
          modifier = Modifier.weight(1f)
        )
        GenderFilterPill(
          title = "Dulhan (Brides)",
          isSelected = selectedGenderTab == "Dulhan",
          onClick = { selectedGenderTab = "Dulhan" },
          modifier = Modifier.weight(1f)
        )
        GenderFilterPill(
          title = "Dulha (Grooms)",
          isSelected = selectedGenderTab == "Dulha",
          onClick = { selectedGenderTab = "Dulha" },
          modifier = Modifier.weight(1f)
        )
      }

      // Quick City Chips
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(quickCities) { city ->
          val isSelected = (city == "All" && selectedCityFilter == null) || (city == selectedCityFilter)
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) GoldPrimary else SurfaceDark)
              .border(
                1.dp,
                if (isSelected) GoldLight else GoldPrimary.copy(alpha = 0.25f),
                RoundedCornerShape(20.dp)
              )
              .clickable {
                selectedCityFilter = if (city == "All") null else city
              }
              .padding(horizontal = 12.dp, vertical = 5.dp)
          ) {
            Text(
              text = city,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              color = if (isSelected) TextDark else TextIvory
            )
          }
        }
      }

      // Profiles List
      if (filteredList.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(top = 40.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "No matching candidate dossiers found",
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp,
            color = TextSand
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Try clearing your filters or register a new candidate dossier.",
            fontSize = 12.sp,
            color = TextMuted
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(filteredList, key = { it.id }) { dossier ->
            DossierCard(
              dossier = dossier,
              onClick = { selectedDossierForDetail = dossier }
            )
          }
        }
      }
    }

    // Floating Action Button to Register New Candidate
    FloatingActionButton(
      onClick = onNavigateToNewRegistration,
      containerColor = GoldPrimary,
      contentColor = TextDark,
      shape = CircleShape,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("fab_register_candidate")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add")
        Spacer(modifier = Modifier.width(4.dp))
        Text("New Dossier", fontWeight = FontWeight.Bold, fontSize = 12.sp)
      }
    }

    // Full Dossier Detail Sheet
    DossierDetailSheet(
      dossier = selectedDossierForDetail,
      onDismiss = { selectedDossierForDetail = null }
    )
  }
}

@Composable
fun GenderFilterPill(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(34.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(
        if (isSelected) {
          Brush.horizontalGradient(listOf(GoldLight, GoldMedium, GoldDark))
        } else {
          Brush.horizontalGradient(listOf(SurfaceDark, SurfaceDark))
        }
      )
      .border(
        1.dp,
        if (isSelected) GoldLight else GoldPrimary.copy(alpha = 0.3f),
        RoundedCornerShape(12.dp)
      )
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      fontFamily = FontFamily.SansSerif,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      fontSize = 11.sp,
      color = if (isSelected) TextDark else TextSand
    )
  }
}

@Composable
fun DossierCard(
  dossier: CandidateDossier,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDulhan = dossier.gender.equals("Dulhan", ignoreCase = true)
  val context = LocalContext.current

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDulhan) Color(0xFF0B1711).copy(alpha = 0.92f) else Color(0xFF131512).copy(alpha = 0.92f)
    ),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isDulhan) EmeraldBorder.copy(alpha = 0.7f) else GoldPrimary.copy(alpha = 0.35f)
    ),
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("dossier_card_${dossier.dossierCode}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Candidate Portrait Avatar
      Box(
        modifier = Modifier
          .size(76.dp)
          .clip(RoundedCornerShape(14.dp))
          .border(
            1.5.dp,
            if (isDulhan) EmeraldBorder else GoldPrimary,
            RoundedCornerShape(14.dp)
          )
      ) {
        AsyncImage(
          model = ImageRequest.Builder(context)
            .data(dossier.frontPortraitUrl.ifBlank { if (isDulhan) AppDatabase.DULHAN_IMAGE_URL else AppDatabase.DULHA_IMAGE_URL })
            .crossfade(true)
            .build(),
          contentDescription = dossier.candidateName,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Content Column
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        // Dossier Code & Gender Badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = dossier.dossierCode,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = GoldLight
          )

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (isDulhan) EmeraldDark else Color(0xFF281E04))
              .border(
                1.dp,
                if (isDulhan) EmeraldBorder else GoldPrimary.copy(alpha = 0.6f),
                RoundedCornerShape(6.dp)
              )
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = dossier.gender,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = if (isDulhan) GoldLight else GoldLight
            )
          }
        }

        // Candidate Name
        Text(
          text = dossier.candidateName,
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = TextIvory,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        // Age, Height, Location
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = "${dossier.city} • ${dossier.age} Yrs • ${dossier.height}",
            fontSize = 11.sp,
            color = TextSand
          )
        }

        // Occupation & Education
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = GoldPrimary,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = dossier.occupation,
            fontSize = 11.sp,
            color = TextIvory,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Text(
          text = "${dossier.maritalStatus} • ${dossier.complexion}",
          fontSize = 10.sp,
          color = GoldMuted
        )
      }
    }
  }
}
