package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import android.widget.Toast
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
import com.example.data.SentProfilesHistoryManager
import com.example.ui.components.JewelDivider
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
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
  onDeleteAllDossiers: () -> Unit = {},
  onPopulateSampleProfiles: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current

  var selectedGenderTab by remember(initialGender) { mutableStateOf(initialGender) }
  var searchQuery by remember { mutableStateOf("") }
  var selectedCityFilter by remember { mutableStateOf<String?>(null) }
  var minAge by remember { mutableStateOf<Int?>(null) }
  var maxAge by remember { mutableStateOf<Int?>(null) }
  var minHeightInches by remember { mutableStateOf<Int?>(null) }
  var maxHeightInches by remember { mutableStateOf<Int?>(null) }
  var selectedMaritalStatusFilter by remember { mutableStateOf<String?>(null) }
  var selectedOccupationFilter by remember { mutableStateOf<String?>(null) }
  var selectedEducationFilter by remember { mutableStateOf<String?>(null) }
  var selectedComplexionFilter by remember { mutableStateOf<String?>(null) }
  var selectedSummaryFilter by remember { mutableStateOf<String?>(null) }

  var showAgeFilterDialog by remember { mutableStateOf(false) }
  var showHeightFilterDialog by remember { mutableStateOf(false) }
  var showCityFilterDialog by remember { mutableStateOf(false) }
  var showMaritalFilterDialog by remember { mutableStateOf(false) }
  var showOccupationFilterDialog by remember { mutableStateOf(false) }
  var showEducationFilterDialog by remember { mutableStateOf(false) }
  var showComplexionFilterDialog by remember { mutableStateOf(false) }

  var selectedDossierForDetail by remember { mutableStateOf<CandidateDossier?>(null) }
  var selectedDossierIds by remember { mutableStateOf(setOf<Long>()) }
  var showProceedDialog by remember { mutableStateOf(false) }
  var showDeleteDialog by remember { mutableStateOf(false) }
  var showSheetSettingsDialog by remember { mutableStateOf(false) }

  var registeredClientPhone by remember { mutableStateOf("") }
  var isRepeatModeAllowed by remember { mutableStateOf(false) } // false = Naye Profiles (New Only), true = Repeat Allowed
  var historyRefreshKey by remember { mutableStateOf(0) }

  val sentCodesForClient = remember(registeredClientPhone, historyRefreshKey) {
    SentProfilesHistoryManager.getSentProfileCodes(context, registeredClientPhone)
  }

  fun isProfileRepeat(dossier: CandidateDossier): Boolean {
    return SentProfilesHistoryManager.isProfileCodeRepeat(sentCodesForClient, dossier.dossierCode)
  }

  fun setRepeatMode(allowed: Boolean) {
    isRepeatModeAllowed = allowed
    if (!allowed && sentCodesForClient.isNotEmpty()) {
      val repeatIds = dossiers.filter { isProfileRepeat(it) }.map { it.id }.toSet()
      val unselectedCount = selectedDossierIds.intersect(repeatIds).size
      selectedDossierIds = selectedDossierIds - repeatIds
      if (unselectedCount > 0) {
        Toast.makeText(
          context,
          "$unselectedCount repeat profile unselect kar diye gaye (Sirf naye profiles select honge)",
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }

  
  val summaryItems = remember(dossiers, initialGender, selectedGenderTab) {
    val baseList = dossiers.filter { dossier ->
      if (initialGender != "All") {
        dossier.gender.equals(initialGender, ignoreCase = true)
      } else {
        when (selectedGenderTab) {
          "Dulhan" -> dossier.gender.equals("Dulhan", ignoreCase = true)
          "Dulha" -> dossier.gender.equals("Dulha", ignoreCase = true)
          else -> true
        }
      }
    }
    val counts = mutableMapOf<String, Int>()
    baseList.forEach { d ->
      if (d.occupation.isNotBlank()) {
        counts[d.occupation] = (counts[d.occupation] ?: 0) + 1
      }
      if (d.education.isNotBlank()) {
        counts[d.education] = (counts[d.education] ?: 0) + 1
      }
    }
    counts.entries.sortedByDescending { it.value }.map { it.key to it.value }
  }

  val hasActiveFilters = minAge != null || maxAge != null || minHeightInches != null || maxHeightInches != null ||
      selectedCityFilter != null || selectedMaritalStatusFilter != null || selectedOccupationFilter != null ||
      selectedEducationFilter != null || selectedComplexionFilter != null || searchQuery.isNotBlank()

  val filteredList = dossiers.filter { dossier ->
    val matchesGender = if (initialGender != "All") {
        dossier.gender.equals(initialGender, ignoreCase = true)
    } else {
        when (selectedGenderTab) {
          "Dulhan" -> dossier.gender.equals("Dulhan", ignoreCase = true)
          "Dulha" -> dossier.gender.equals("Dulha", ignoreCase = true)
          else -> true
        }
    }

    val matchesAge = (minAge == null || dossier.age >= minAge!!) &&
        (maxAge == null || dossier.age <= maxAge!!)

    val dossierHeightInches = parseHeightToInches(dossier.height)
    val matchesHeight = if (minHeightInches == null && maxHeightInches == null) {
      true
    } else if (dossierHeightInches == null) {
      true
    } else {
      (minHeightInches == null || dossierHeightInches >= minHeightInches!!) &&
      (maxHeightInches == null || dossierHeightInches <= maxHeightInches!!)
    }

    val matchesCity = if (selectedCityFilter == null || selectedCityFilter == "All") true 
      else dossier.city.contains(selectedCityFilter!!, ignoreCase = true)

    val matchesMarital = if (selectedMaritalStatusFilter == null || selectedMaritalStatusFilter == "All") true 
      else dossier.maritalStatus.contains(selectedMaritalStatusFilter!!, ignoreCase = true)

    val matchesOccupation = if (selectedOccupationFilter == null || selectedOccupationFilter == "All") true 
      else if (selectedOccupationFilter == "Software") {
        dossier.occupation.contains("Software", ignoreCase = true) ||
        dossier.occupation.contains("Developer", ignoreCase = true) ||
        dossier.occupation.contains("Programmer", ignoreCase = true) ||
        dossier.occupation.contains("Engineer", ignoreCase = true)
      } else if (selectedOccupationFilter == "Advocate") {
        dossier.occupation.contains("Advocate", ignoreCase = true) ||
        dossier.occupation.contains("Lawyer", ignoreCase = true)
      } else if (selectedOccupationFilter == "Business") {
        dossier.occupation.contains("Business", ignoreCase = true) ||
        dossier.occupation.contains("Trader", ignoreCase = true)
      } else if (selectedOccupationFilter == "Govt") {
        dossier.occupation.contains("Govt", ignoreCase = true) ||
        dossier.occupation.contains("Government", ignoreCase = true)
      } else if (selectedOccupationFilter == "Bank") {
        dossier.occupation.contains("Bank", ignoreCase = true)
      } else dossier.occupation.contains(selectedOccupationFilter!!, ignoreCase = true)

    val matchesEducation = if (selectedEducationFilter == null || selectedEducationFilter == "All") true 
      else if (selectedEducationFilter == "Master") {
        dossier.education.contains("Master", ignoreCase = true) ||
        dossier.education.contains("M.Sc", ignoreCase = true) ||
        dossier.education.contains("MCA", ignoreCase = true) ||
        dossier.education.contains("MBA", ignoreCase = true) ||
        dossier.education.contains("M.Tech", ignoreCase = true) ||
        dossier.education.contains("M.E", ignoreCase = true) ||
        dossier.education.contains("Post", ignoreCase = true)
      } else if (selectedEducationFilter == "Graduate") {
        dossier.education.contains("Graduate", ignoreCase = true) ||
        dossier.education.contains("Degree", ignoreCase = true) ||
        dossier.education.contains("B.A", ignoreCase = true) ||
        dossier.education.contains("B.Com", ignoreCase = true) ||
        dossier.education.contains("B.Sc", ignoreCase = true) ||
        dossier.education.contains("B.E", ignoreCase = true) ||
        dossier.education.contains("B.Tech", ignoreCase = true) ||
        dossier.education.contains("BBA", ignoreCase = true) ||
        dossier.education.contains("BCA", ignoreCase = true)
      } else if (selectedEducationFilter == "12th") {
        dossier.education.contains("12th", ignoreCase = true) ||
        dossier.education.contains("HSC", ignoreCase = true) ||
        dossier.education.contains("Intermediate", ignoreCase = true)
      } else if (selectedEducationFilter == "10th") {
        dossier.education.contains("10th", ignoreCase = true) ||
        dossier.education.contains("SSC", ignoreCase = true) ||
        dossier.education.contains("Matric", ignoreCase = true)
      } else if (selectedEducationFilter == "Diploma") {
        dossier.education.contains("Diploma", ignoreCase = true) ||
        dossier.education.contains("Polytechnic", ignoreCase = true)
      } else dossier.education.contains(selectedEducationFilter!!, ignoreCase = true)

    val matchesComplexion = if (selectedComplexionFilter == null || selectedComplexionFilter == "All") true 
      else dossier.complexion.contains(selectedComplexionFilter!!, ignoreCase = true)

    val matchesQuery = if (searchQuery.isBlank()) true else {
      dossier.candidateName.contains(searchQuery, ignoreCase = true) ||
        dossier.dossierCode.contains(searchQuery, ignoreCase = true) ||
        dossier.city.contains(searchQuery, ignoreCase = true) ||
        dossier.occupation.contains(searchQuery, ignoreCase = true) ||
        dossier.education.contains(searchQuery, ignoreCase = true)
    }

    val matchesSummary = if (selectedSummaryFilter == null) true else (
      dossier.occupation.contains(selectedSummaryFilter!!, ignoreCase = true) ||
        dossier.education.contains(selectedSummaryFilter!!, ignoreCase = true)
    )
    matchesGender && matchesAge && matchesHeight && matchesCity && matchesMarital &&
      matchesOccupation && matchesEducation && matchesComplexion && matchesQuery && matchesSummary
  }

  val quickCities = listOf(
    "All",
    "Solapur",
    "Bijapur",
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
      // Top Bar with Prominent Back Button (Edge-to-edge statusBarsPadding)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Regular Circular Back Button with Arrow matching KhanHeader style
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(RoyalBlack.copy(alpha = 0.88f))
            .border(1.5.dp, GoldPrimary, CircleShape)
            .clickable { onBack() }
            .testTag("search_back_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = GoldLight,
            modifier = Modifier.size(22.dp)
          )
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
            text = if (initialGender != "All") "${initialGender.uppercase()} MATCHES" else "VERIFIED MATCHES",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            color = TextSand
          )
        }

        // Gender Avatar matching KhanHeader style copied from home selection
        val effectiveGender = if (initialGender != "All") initialGender else selectedGenderTab
        val avatarUrl = if (effectiveGender.equals("Dulhan", ignoreCase = true)) {
          AppDatabase.DULHAN_IMAGE_URL
        } else {
          AppDatabase.DULHA_IMAGE_URL
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .border(2.dp, GoldPrimary, CircleShape)
            .border(4.dp, GoldPrimary.copy(alpha = 0.25f), CircleShape)
            .background(RoyalBlack)
            .clickable { onBack() }
            .testTag("search_gender_avatar"),
          contentAlignment = Alignment.Center
        ) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(avatarUrl)
              .crossfade(true)
              .build(),
            contentDescription = "Selected Gender Avatar",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Placeholder to maintain spacing if needed
        }
      }

      // 0. Registered Client Mobile Number Input & History Tracker (At Top of Search Screen)
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131713)),
        border = BorderStroke(1.2.dp, if (sentCodesForClient.isNotEmpty()) GoldPrimary else GoldPrimary.copy(alpha = 0.45f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Registered Client Mobile Number",
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp,
                color = GoldLight
              )
            }
            if (registeredClientPhone.isNotBlank()) {
              Text(
                text = "Clear",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9E9E),
                modifier = Modifier
                  .clickable { registeredClientPhone = "" }
                  .padding(4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          OutlinedTextField(
            value = registeredClientPhone,
            onValueChange = { input -> registeredClientPhone = input },
            placeholder = {
              Text("Enter 10-digit mobile (e.g. 9876543210)", fontSize = 11.5.sp, color = TextMuted)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = GoldLight,
              unfocusedBorderColor = GoldPrimary.copy(alpha = 0.4f),
              focusedTextColor = TextIvory,
              unfocusedTextColor = TextIvory,
              focusedContainerColor = Color(0xFF1A201A),
              unfocusedContainerColor = Color(0xFF1A201A)
            ),
            trailingIcon = {
              if (registeredClientPhone.isBlank()) {
                Text(
                  text = "Demo: 9876543210",
                  fontSize = 10.sp,
                  color = GoldLight,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF2B220C))
                    .clickable { registeredClientPhone = "9876543210" }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                )
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_registered_client_mobile")
          )

          // History and Toggle (shows immediately when phone is entered)
          if (registeredClientPhone.trim().length >= 7) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = GoldPrimary.copy(alpha = 0.2f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(6.dp))

            if (sentCodesForClient.isNotEmpty()) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Abhi tak bheje gaye profiles: ${sentCodesForClient.size} Total",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                items(sentCodesForClient.toList()) { code ->
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Color(0xFF2B2510))
                      .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "Profile #$code",
                      fontSize = 10.5.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = GoldLight
                    )
                  }
                }
              }
            } else {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldLight, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Naya Client: Abhi tak 0 profile bheji gayi hai",
                  fontSize = 11.sp,
                  color = EmeraldLight
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Two-Option Toggle Button: [✨ Naye Profiles (New)] vs [🔄 Repeat Bhejein]
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0D120E))
                .border(1.dp, GoldPrimary.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                .padding(4.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (!isRepeatModeAllowed) GoldPrimary else Color.Transparent)
                  .clickable { setRepeatMode(false) },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "✨ Naye Profiles (New)",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (!isRepeatModeAllowed) TextDark else TextSand
                )
              }

              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(34.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isRepeatModeAllowed) Color(0xFFD4AF37) else Color.Transparent)
                  .clickable { setRepeatMode(true) },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "🔄 Repeat Bhejein",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isRepeatModeAllowed) TextDark else TextSand
                )
              }
            }
          }
        }
      }

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
              .background(GoldPrimary.copy(alpha = if (searchQuery.isNotBlank()) 0.25f else 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search icon",
              tint = if (searchQuery.isNotBlank()) GoldLight else GoldPrimary,
              modifier = Modifier.size(18.dp)
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
              .background(if (hasActiveFilters) GoldPrimary.copy(alpha = 0.8f) else GoldMuted.copy(alpha = 0.5f))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (searchQuery.isNotBlank()) {
              "Searching \"$searchQuery\""
            } else if (hasActiveFilters) {
              "Filtered ${if (initialGender != "All") initialGender else if (selectedGenderTab == "All") "All" else selectedGenderTab} Matches"
            } else {
              "Select a filter to view ${if (initialGender != "All") initialGender else if (selectedGenderTab == "All") "All" else selectedGenderTab} profiles"
            },
            fontSize = 11.sp,
            color = GoldMuted,
            fontWeight = FontWeight.Medium
          )
        }

        AnimatedContent(
          targetState = if (hasActiveFilters) filteredList.size else 0,
          label = "CountAnimation"
        ) { count ->
          Text(
            text = if (hasActiveFilters) "$count Profiles" else "0 Profiles",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (hasActiveFilters) GoldLight else TextMuted
          )
        }
      }

      // Gender Category Tabs: "All", "Dulhan (Brides)", "Dulha (Grooms)"
      if (initialGender == "All") {
        val dulhanCount = dossiers.count { it.gender.equals("Dulhan", ignoreCase = true) }
        val dulhaCount = dossiers.count { it.gender.equals("Dulha", ignoreCase = true) }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          GenderFilterPill(
            title = "All Profiles (${dossiers.size})",
            isSelected = selectedGenderTab == "All",
            onClick = {
              selectedGenderTab = "All"
              selectedOccupationFilter = null
              selectedEducationFilter = null
              selectedDossierIds = emptySet()
            },
            modifier = Modifier.weight(1f)
          )
          GenderFilterPill(
            title = "Dulhan ($dulhanCount)",
            isSelected = selectedGenderTab == "Dulhan",
            onClick = {
              selectedGenderTab = "Dulhan"
              selectedOccupationFilter = null
              selectedEducationFilter = null
              selectedDossierIds = emptySet()
            },
            modifier = Modifier.weight(1f)
          )
          GenderFilterPill(
            title = "Dulha ($dulhaCount)",
            isSelected = selectedGenderTab == "Dulha",
            onClick = {
              selectedGenderTab = "Dulha"
              selectedOccupationFilter = null
              selectedEducationFilter = null
              selectedDossierIds = emptySet()
            },
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Registration Form Style Filter Buttons Row (Below Search Bar)
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // 1. Age Range Filter Button (Custom Range e.g. 25 to 30)
        item {
          val ageLabel = if (minAge != null && maxAge != null) {
            "$minAge - $maxAge Yrs"
          } else if (minAge != null) {
            "$minAge+ Yrs"
          } else if (maxAge != null) {
            "Upto $maxAge Yrs"
          } else {
            null
          }
          RegistrationFilterButton(
            label = "Age Range",
            selectedValue = ageLabel,
            icon = Icons.Default.Cake,
            isActive = minAge != null || maxAge != null,
            onClick = { showAgeFilterDialog = true }
          )
        }

        // 2. Height Range Filter Button (Custom Range e.g. 5'0" to 5'8")
        item {
          val heightLabel = if (minHeightInches != null && maxHeightInches != null) {
            "${formatInchesToHeight(minHeightInches!!)} - ${formatInchesToHeight(maxHeightInches!!)}"
          } else if (minHeightInches != null) {
            "${formatInchesToHeight(minHeightInches!!)}+"
          } else if (maxHeightInches != null) {
            "Upto ${formatInchesToHeight(maxHeightInches!!)}"
          } else {
            null
          }
          RegistrationFilterButton(
            label = "Height",
            selectedValue = heightLabel,
            icon = Icons.Default.Height,
            isActive = minHeightInches != null || maxHeightInches != null,
            onClick = { showHeightFilterDialog = true }
          )
        }

        // 3. City / Location Filter Button
        item {
          RegistrationFilterButton(
            label = "City",
            selectedValue = selectedCityFilter,
            icon = Icons.Default.LocationCity,
            isActive = selectedCityFilter != null,
            onClick = { showCityFilterDialog = true }
          )
        }

        // 4. Marital Status Filter Button
        item {
          RegistrationFilterButton(
            label = "Marital",
            selectedValue = selectedMaritalStatusFilter,
            icon = Icons.Default.FavoriteBorder,
            isActive = selectedMaritalStatusFilter != null,
            onClick = { showMaritalFilterDialog = true }
          )
        }

        // 5. Occupation Filter Button
        item {
          RegistrationFilterButton(
            label = "Occupation",
            selectedValue = selectedOccupationFilter,
            icon = Icons.Default.WorkOutline,
            isActive = selectedOccupationFilter != null,
            onClick = { showOccupationFilterDialog = true }
          )
        }

        // 6. Education Filter Button
        item {
          RegistrationFilterButton(
            label = "Education",
            selectedValue = selectedEducationFilter,
            icon = Icons.Default.School,
            isActive = selectedEducationFilter != null,
            onClick = { showEducationFilterDialog = true }
          )
        }

        // 7. Complexion Filter Button
        item {
          RegistrationFilterButton(
            label = "Complexion",
            selectedValue = selectedComplexionFilter,
            icon = Icons.Default.Palette,
            isActive = selectedComplexionFilter != null,
            onClick = { showComplexionFilterDialog = true }
          )
        }

        // Reset All Button
        if (hasActiveFilters) {
          item {
            Box(
              modifier = Modifier
                .height(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF261212))
                .border(1.dp, Color(0xFFE57373), RoundedCornerShape(12.dp))
                .clickable {
                  minAge = null
                  maxAge = null
                  minHeightInches = null
                  maxHeightInches = null
                  selectedCityFilter = null
                  selectedMaritalStatusFilter = null
                  selectedOccupationFilter = null
                  selectedEducationFilter = null
                  selectedComplexionFilter = null
                }
                .padding(horizontal = 12.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.RestartAlt,
                  contentDescription = "Reset",
                  tint = Color(0xFFFFB4B4),
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Reset All",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFFFB4B4)
                )
              }
            }
          }
        }
      }

        // Dynamic Category Lists for current gender
      val currentGenderDossiers = remember(dossiers, initialGender, selectedGenderTab) {
        dossiers.filter { dossier ->
          if (initialGender != "All") {
            dossier.gender.equals(initialGender, ignoreCase = true)
          } else {
            when (selectedGenderTab) {
              "Dulhan" -> dossier.gender.equals("Dulhan", ignoreCase = true)
              "Dulha" -> dossier.gender.equals("Dulha", ignoreCase = true)
              else -> true
            }
          }
        }
      }

      val occupationCategories = remember(currentGenderDossiers) {
        val standard = listOf(
          CategoryFilterItem("Teacher", "Teacher", currentGenderDossiers.count { it.occupation.contains("Teacher", ignoreCase = true) }),
          CategoryFilterItem("Doctor", "Doctor", currentGenderDossiers.count { it.occupation.contains("Doctor", ignoreCase = true) }),
          CategoryFilterItem(
            "Software/IT",
            "Software",
            currentGenderDossiers.count {
              it.occupation.contains("Software", ignoreCase = true) ||
              it.occupation.contains("Developer", ignoreCase = true) ||
              it.occupation.contains("Programmer", ignoreCase = true) ||
              it.occupation.contains("Engineer", ignoreCase = true)
            }
          ),
          CategoryFilterItem("Advocate", "Advocate", currentGenderDossiers.count { it.occupation.contains("Advocate", ignoreCase = true) || it.occupation.contains("Lawyer", ignoreCase = true) }),
          CategoryFilterItem("Business", "Business", currentGenderDossiers.count { it.occupation.contains("Business", ignoreCase = true) || it.occupation.contains("Trader", ignoreCase = true) }),
          CategoryFilterItem("Govt Job", "Govt", currentGenderDossiers.count { it.occupation.contains("Govt", ignoreCase = true) || it.occupation.contains("Government", ignoreCase = true) }),
          CategoryFilterItem("Banker", "Bank", currentGenderDossiers.count { it.occupation.contains("Bank", ignoreCase = true) })
        ).filter { it.count > 0 }

        val knownOccKeys = listOf("teacher", "doctor", "software", "developer", "programmer", "engineer", "advocate", "lawyer", "business", "trader", "govt", "government", "bank")
        val others = currentGenderDossiers
          .map { it.occupation.trim() }
          .filter { occ -> occ.isNotBlank() && knownOccKeys.none { key -> occ.contains(key, ignoreCase = true) } }
          .groupingBy { it }
          .eachCount()
          .map { (occ, count) ->
            val label = if (occ.length > 18) occ.take(16) + "…" else occ
            CategoryFilterItem(label, occ, count)
          }
        standard + others
      }

      val educationCategories = remember(currentGenderDossiers) {
        val standard = listOf(
          CategoryFilterItem(
            "Post Graduate / M.Sc",
            "Master",
            currentGenderDossiers.count {
              it.education.contains("Master", ignoreCase = true) ||
              it.education.contains("M.Sc", ignoreCase = true) ||
              it.education.contains("MCA", ignoreCase = true) ||
              it.education.contains("MBA", ignoreCase = true) ||
              it.education.contains("M.Tech", ignoreCase = true) ||
              it.education.contains("M.E", ignoreCase = true) ||
              it.education.contains("Post", ignoreCase = true)
            }
          ),
          CategoryFilterItem(
            "Graduate / B.A / B.Com",
            "Graduate",
            currentGenderDossiers.count {
              it.education.contains("Graduate", ignoreCase = true) ||
              it.education.contains("Degree", ignoreCase = true) ||
              it.education.contains("B.A", ignoreCase = true) ||
              it.education.contains("B.Com", ignoreCase = true) ||
              it.education.contains("B.Sc", ignoreCase = true) ||
              it.education.contains("B.E", ignoreCase = true) ||
              it.education.contains("B.Tech", ignoreCase = true) ||
              it.education.contains("BBA", ignoreCase = true) ||
              it.education.contains("BCA", ignoreCase = true)
            }
          ),
          CategoryFilterItem("12th Pass", "12th", currentGenderDossiers.count { it.education.contains("12th", ignoreCase = true) || it.education.contains("HSC", ignoreCase = true) || it.education.contains("Intermediate", ignoreCase = true) }),
          CategoryFilterItem("10th Pass", "10th", currentGenderDossiers.count { it.education.contains("10th", ignoreCase = true) || it.education.contains("SSC", ignoreCase = true) || it.education.contains("Matric", ignoreCase = true) }),
          CategoryFilterItem("Diploma", "Diploma", currentGenderDossiers.count { it.education.contains("Diploma", ignoreCase = true) || it.education.contains("Polytechnic", ignoreCase = true) })
        ).filter { it.count > 0 }

        val knownEduKeys = listOf("master", "m.sc", "mca", "mba", "m.tech", "m.e", "post", "graduate", "degree", "b.a", "b.com", "b.sc", "b.e", "b.tech", "bba", "bca", "12th", "hsc", "intermediate", "10th", "ssc", "matric", "diploma", "polytechnic")
        val others = currentGenderDossiers
          .map { it.education.trim() }
          .filter { edu -> edu.isNotBlank() && knownEduKeys.none { key -> edu.contains(key, ignoreCase = true) } }
          .groupingBy { it }
          .eachCount()
          .map { (edu, count) ->
            val label = if (edu.length > 18) edu.take(16) + "…" else edu
            CategoryFilterItem(label, edu, count)
          }
        standard + others
      }

      // Row 1: Profession / Occupation Filter Buttons (Only shown if candidates exist in Google Drive/DB)
      if (occupationCategories.isNotEmpty()) {
        Column(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.WorkOutline, contentDescription = null, tint = GoldLight, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Profession / Occupation", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSand)
          }
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(occupationCategories, key = { "occ_${it.filterKey}" }) { cat ->
              CategoryFilterButton(
                label = cat.label,
                count = cat.count,
                isSelected = selectedOccupationFilter == cat.filterKey,
                onClick = {
                  selectedOccupationFilter = if (selectedOccupationFilter == cat.filterKey) null else cat.filterKey
                }
              )
            }
          }
        }
      }

      // Row 2: Education Filter Buttons (Only shown if candidates exist in Google Drive/DB)
      if (educationCategories.isNotEmpty()) {
        Column(modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.School, contentDescription = null, tint = GoldLight, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(5.dp))
            Text("Education / Qualification", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSand)
          }
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(educationCategories, key = { "edu_${it.filterKey}" }) { cat ->
              CategoryFilterButton(
                label = cat.label,
                count = cat.count,
                isSelected = selectedEducationFilter == cat.filterKey,
                onClick = {
                  selectedEducationFilter = if (selectedEducationFilter == cat.filterKey) null else cat.filterKey
                }
              )
            }
          }
        }
      }

      // Active Filter Badges with Quick Removal (✕)
      if (hasActiveFilters) {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          if (minAge != null || maxAge != null) {
            item {
              ActiveFilterChip(
                text = "Age: ${minAge ?: 18}-${maxAge ?: 55} yrs",
                onDismiss = {
                  minAge = null
                  maxAge = null
                }
              )
            }
          }
          if (minHeightInches != null || maxHeightInches != null) {
            item {
              ActiveFilterChip(
                text = "Height: ${formatInchesToHeight(minHeightInches ?: 58)}-${formatInchesToHeight(maxHeightInches ?: 76)}",
                onDismiss = {
                  minHeightInches = null
                  maxHeightInches = null
                }
              )
            }
          }
          if (selectedCityFilter != null) {
            item {
              ActiveFilterChip(
                text = "City: $selectedCityFilter",
                onDismiss = { selectedCityFilter = null }
              )
            }
          }
          if (selectedMaritalStatusFilter != null) {
            item {
              ActiveFilterChip(
                text = "Marital: $selectedMaritalStatusFilter",
                onDismiss = { selectedMaritalStatusFilter = null }
              )
            }
          }
          if (selectedOccupationFilter != null) {
            item {
              ActiveFilterChip(
                text = "Job: $selectedOccupationFilter",
                onDismiss = { selectedOccupationFilter = null }
              )
            }
          }
          if (selectedEducationFilter != null) {
            item {
              ActiveFilterChip(
                text = "Edu: $selectedEducationFilter",
                onDismiss = { selectedEducationFilter = null }
              )
            }
          }
          if (selectedComplexionFilter != null) {
            item {
              ActiveFilterChip(
                text = "Color: $selectedComplexionFilter",
                onDismiss = { selectedComplexionFilter = null }
              )
            }
          }
        }
      }

      // Profiles List (Only shown when a filter or search is active)
      if (!hasActiveFilters) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(top = 40.dp, start = 24.dp, end = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(Color(0xFF1E1A11))
              .border(1.2.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = null,
              tint = GoldLight,
              modifier = Modifier.size(26.dp)
            )
          }
          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Filter select karein",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = GoldLight
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Profiles dekhne ke liye uppar se koi Profession, Education ya Filter select karein.",
            fontSize = 12.5.sp,
            color = TextSand,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      } else if (filteredList.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(top = 40.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = if (dossiers.isEmpty()) "Koi profile uplabdh nahi hai" else "No matching candidate dossiers found",
            fontFamily = FontFamily.Serif,
            fontSize = 15.sp,
            color = TextSand
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Try changing your selected filters or search terms.",
            fontSize = 12.sp,
            color = TextMuted
          )
          if (dossiers.isEmpty()) {
            Spacer(modifier = Modifier.height(14.dp))
            Button(
              onClick = onPopulateSampleProfiles,
              colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = TextDark),
              shape = RoundedCornerShape(20.dp),
              modifier = Modifier.testTag("button_populate_samples")
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Fill Sample Dulha & Dulhan Profiles (With Images)", fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
            }
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          items(filteredList, key = { it.id }) { dossier ->
            val isSelected = selectedDossierIds.contains(dossier.id)
            val isRepeat = isProfileRepeat(dossier)
            DossierCard(
              dossier = dossier,
              isSelected = isSelected,
              isRepeat = isRepeat,
              repeatModeAllowed = isRepeatModeAllowed,
              onToggleSelect = {
                if (!isSelected && isRepeat && !isRepeatModeAllowed) {
                  Toast.makeText(
                    context,
                    "Profile #${dossier.dossierCode} pehle bheja gaya hai! Ise bhejne ke liye 'Repeat Bhejein' mode ON karein.",
                    Toast.LENGTH_SHORT
                  ).show()
                } else {
                  selectedDossierIds = if (isSelected) {
                    selectedDossierIds - dossier.id
                  } else {
                    selectedDossierIds + dossier.id
                  }
                }
              },
              onClick = { selectedDossierForDetail = dossier }
            )
          }
        }
      }
    }

    // Full Dossier Detail Sheet
    DossierDetailSheet(
      dossier = selectedDossierForDetail,
      onDismiss = { selectedDossierForDetail = null }
    )

    // Confirmation Dialog for Clearing All Profiles
    if (showDeleteDialog) {
      AlertDialog(
        onDismissRequest = { showDeleteDialog = false },
        containerColor = Color(0xFF1A1414),
        titleContentColor = Color(0xFFFF8A8A),
        textContentColor = TextIvory,
        title = { Text("Delete All Profiles?", fontWeight = FontWeight.Bold) },
        text = {
          Text(
            "Are you sure you want to clear all profiles from the app? You can add fresh registrations for Dulha and Dulhan with photos.",
            fontSize = 13.sp
          )
        },
        confirmButton = {
          Button(
            onClick = {
              showDeleteDialog = false
              onDeleteAllDossiers()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB52B2B))
          ) {
            Text("Yes, Delete All", color = Color.White, fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { showDeleteDialog = false }) {
            Text("Cancel", color = GoldLight)
          }
        }
      )
    }

    // Google Sheets Connection & Test Entry Dialog
    if (showSheetSettingsDialog) {
      val scope = rememberCoroutineScope()
      var tempSheetUrl by remember {
        mutableStateOf(
          com.example.data.GoogleSheetsDriveService.customSpreadsheetUrl.ifBlank {
            "https://docs.google.com/spreadsheets/d/${com.example.data.GoogleSheetsDriveService.SPREADSHEET_ID}/edit?usp=sharing"
          }
        )
      }
      var tempWebhookUrl by remember {
        mutableStateOf(com.example.data.GoogleSheetsDriveService.appsScriptWebhookUrl)
      }
      var testStatusMessage by remember { mutableStateOf<String?>(null) }
      var isTesting by remember { mutableStateOf(false) }

      AlertDialog(
        onDismissRequest = { showSheetSettingsDialog = false },
        containerColor = Color(0xFF141714),
        titleContentColor = GoldLight,
        textContentColor = TextIvory,
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = GoldLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Google Sheet Connection", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }
        },
        text = {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
              text = "Paste your exact complete Google Sheet URL below. (Tabs required: 'Dulha (Groom)' and 'Dulhan (Bride)').",
              fontSize = 11.5.sp,
              color = TextSand
            )

            OutlinedTextField(
              value = tempSheetUrl,
              onValueChange = {
                tempSheetUrl = it
                com.example.data.GoogleSheetsDriveService.customSpreadsheetUrl = it
              },
              label = { Text("Google Sheet URL", color = GoldLight, fontSize = 11.sp) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldLight,
                unfocusedBorderColor = GoldLight.copy(alpha = 0.5f),
                focusedTextColor = TextIvory,
                unfocusedTextColor = TextIvory
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().testTag("input_sheet_url")
            )

            OutlinedTextField(
              value = tempWebhookUrl,
              onValueChange = {
                tempWebhookUrl = it
                com.example.data.GoogleSheetsDriveService.appsScriptWebhookUrl = it
              },
              label = { Text("Optional Apps Script Webhook URL", color = GoldLight, fontSize = 11.sp) },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldLight,
                unfocusedBorderColor = GoldLight.copy(alpha = 0.5f),
                focusedTextColor = TextIvory,
                unfocusedTextColor = TextIvory
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth().testTag("input_webhook_url")
            )

            Button(
              onClick = {
                isTesting = true
                testStatusMessage = "Sending test entries to sheet..."
                scope.launch {
                  val result = com.example.data.GoogleSheetsDriveService.sendDirectTestEntriesToSheet(context)
                  isTesting = false
                  testStatusMessage = if (result.first) "✅ " + result.second else "⚠️ " + result.second
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = TextDark),
              shape = RoundedCornerShape(10.dp),
              enabled = !isTesting,
              modifier = Modifier.fillMaxWidth().testTag("button_test_sheet_connection")
            ) {
              Text(if (isTesting) "Sending..." else "🚀 Send Test Entry to Sheet", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            testStatusMessage?.let { msg ->
              Text(
                text = msg,
                fontSize = 11.sp,
                color = if (msg.startsWith("✅")) GoldLight else Color(0xFFFFB2B2),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        },
        confirmButton = {
          Button(
            onClick = {
              com.example.data.GoogleSheetsDriveService.customSpreadsheetUrl = tempSheetUrl
              com.example.data.GoogleSheetsDriveService.appsScriptWebhookUrl = tempWebhookUrl
              showSheetSettingsDialog = false
            },
            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = TextDark)
          ) {
            Text("Save Link", fontWeight = FontWeight.Bold)
          }
        },
        dismissButton = {
          TextButton(onClick = { showSheetSettingsDialog = false }) {
            Text("Close", color = GoldLight)
          }
        }
      )
    }

    // 1. Age Range Filter BottomSheet
    if (showAgeFilterDialog) {
      AgeRangeFilterBottomSheet(
        currentMinAge = minAge,
        currentMaxAge = maxAge,
        onApply = { min, max ->
          minAge = min
          maxAge = max
          showAgeFilterDialog = false
        },
        onClear = {
          minAge = null
          maxAge = null
          showAgeFilterDialog = false
        },
        onDismiss = { showAgeFilterDialog = false }
      )
    }

    // 2. Height Range Filter BottomSheet
    if (showHeightFilterDialog) {
      HeightRangeFilterBottomSheet(
        currentMinInches = minHeightInches,
        currentMaxInches = maxHeightInches,
        onApply = { min, max ->
          minHeightInches = min
          maxHeightInches = max
          showHeightFilterDialog = false
        },
        onClear = {
          minHeightInches = null
          maxHeightInches = null
          showHeightFilterDialog = false
        },
        onDismiss = { showHeightFilterDialog = false }
      )
    }

    // 3. City Filter BottomSheet
    if (showCityFilterDialog) {
      SingleSelectFilterBottomSheet(
        title = "City / Location",
        icon = Icons.Default.LocationCity,
        selectedOption = selectedCityFilter,
        options = filterCityOptions,
        onSelect = {
          selectedCityFilter = it
          showCityFilterDialog = false
        },
        onDismiss = { showCityFilterDialog = false }
      )
    }

    // 4. Marital Status Filter BottomSheet
    if (showMaritalFilterDialog) {
      SingleSelectFilterBottomSheet(
        title = "Marital Status",
        icon = Icons.Default.FavoriteBorder,
        selectedOption = selectedMaritalStatusFilter,
        options = filterMaritalOptions,
        onSelect = {
          selectedMaritalStatusFilter = it
          showMaritalFilterDialog = false
        },
        onDismiss = { showMaritalFilterDialog = false }
      )
    }

    // 5. Occupation Filter BottomSheet
    if (showOccupationFilterDialog) {
      SingleSelectFilterBottomSheet(
        title = "Occupation",
        icon = Icons.Default.WorkOutline,
        selectedOption = selectedOccupationFilter,
        options = filterOccupationOptions,
        onSelect = {
          selectedOccupationFilter = it
          showOccupationFilterDialog = false
        },
        onDismiss = { showOccupationFilterDialog = false }
      )
    }

    // 6. Education Filter BottomSheet
    if (showEducationFilterDialog) {
      SingleSelectFilterBottomSheet(
        title = "Education",
        icon = Icons.Default.School,
        selectedOption = selectedEducationFilter,
        options = filterEducationOptions,
        onSelect = {
          selectedEducationFilter = it
          showEducationFilterDialog = false
        },
        onDismiss = { showEducationFilterDialog = false }
      )
    }

    // 7. Complexion Filter BottomSheet
    if (showComplexionFilterDialog) {
      SingleSelectFilterBottomSheet(
        title = "Complexion (Color)",
        icon = Icons.Default.Palette,
        selectedOption = selectedComplexionFilter,
        options = filterComplexionOptions,
        onSelect = {
          selectedComplexionFilter = it
          showComplexionFilterDialog = false
        },
        onDismiss = { showComplexionFilterDialog = false }
      )
    }

    // Docked Bottom Proceed Bar when candidate profiles are selected
    AnimatedVisibility(
      visible = selectedDossierIds.isNotEmpty(),
      enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
      exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
    ) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF141914),
        border = BorderStroke(1.5.dp, GoldPrimary),
        shadowElevation = 10.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            val selectedList = dossiers.filter { selectedDossierIds.contains(it.id) }
            val repeatCount = selectedList.count { isProfileRepeat(it) }
            val newCount = selectedList.size - repeatCount

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${selectedDossierIds.size} Selected",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = GoldLight
              )
              if (repeatCount > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "($newCount New, $repeatCount Repeat)",
                  fontSize = 10.5.sp,
                  color = if (isRepeatModeAllowed) GoldLight else Color(0xFFFFB4B4),
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            val selectedCodes = selectedList.joinToString(", ") { it.dossierCode }
            Text(
              text = if (isRepeatModeAllowed) "Mode: Repeat Allowed • $selectedCodes" else "Mode: New Only • $selectedCodes",
              fontSize = 10.sp,
              color = TextSand,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Clear",
              color = Color(0xFFFF9E9E),
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier
                .clickable { selectedDossierIds = emptySet() }
                .padding(horizontal = 6.dp, vertical = 6.dp)
            )

            Button(
              onClick = { showProceedDialog = true },
              colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = TextDark
              ),
              shape = RoundedCornerShape(12.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
              modifier = Modifier.testTag("button_proceed_selection")
            ) {
              Text("Proceed", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(15.dp)
              )
            }
          }
        }
      }
    }

    // Modal Bottom Sheet: Proceed and Send to WhatsApp
    if (showProceedDialog) {
      val selectedDossierList = dossiers.filter { selectedDossierIds.contains(it.id) }
      ProceedShareDialog(
        initialPhoneNumber = registeredClientPhone,
        selectedProfiles = selectedDossierList,
        initialRepeatModeAllowed = isRepeatModeAllowed,
        isProfileRepeat = { isProfileRepeat(it) },
        onToggleProfile = { id ->
          selectedDossierIds = if (selectedDossierIds.contains(id)) {
            selectedDossierIds - id
          } else {
            selectedDossierIds + id
          }
        },
        onDismiss = { showProceedDialog = false },
        onSendWhatsApp = { phone, profiles ->
          sendProfilesViaWhatsApp(context, phone, profiles)
          SentProfilesHistoryManager.saveSentProfileCodes(context, phone, profiles.map { it.dossierCode })
          historyRefreshKey++
          showProceedDialog = false
          selectedDossierIds = emptySet()
          Toast.makeText(context, "${profiles.size} profiles WhatsApp par bheje gaye! History update ho gayi.", Toast.LENGTH_LONG).show()
        }
      )
    }
  }
}

// Helpers for Height calculation
fun parseHeightToInches(heightStr: String?): Int? {
  if (heightStr.isNullOrBlank()) return null
  val regex = """(\d+)\s*['ft\s\.-]+\s*(\d+)?""".toRegex()
  val match = regex.find(heightStr)
  if (match != null) {
    val feet = match.groupValues[1].toIntOrNull() ?: return null
    val inches = match.groupValues.getOrNull(2)?.toIntOrNull() ?: 0
    if (feet in 3..7 && inches in 0..12) {
      return feet * 12 + inches
    }
  }
  val num = heightStr.filter { it.isDigit() }.toIntOrNull()
  if (num != null && num in 48..84) return num
  return null
}

fun formatInchesToHeight(inches: Int): String {
  val ft = inches / 12
  val inch = inches % 12
  return "$ft'$inch\""
}

val filterCityOptions = listOf(
  "Solapur",
    "Bijapur", "Mumbai", "Pune", "Akkalkot", "Pandharpur", "Sangli", "Satara", "Osmanabad", "Gulbarga", "Hyderabad"
)

val filterMaritalOptions = listOf(
  "Never Married", "Divorced", "Widowed", "Khula / Separated", "Second Marriage"
)

val filterOccupationOptions = listOf(
  "Doctor", "Engineer", "Software Professional / IT", "Businessman", "Businesswoman",
  "Chartered Accountant", "Teacher / Professor", "Government / Public Service", "Homemaker", "Other"
)

val filterEducationOptions = listOf(
  "Doctor", "Engineer", "Post-Graduate", "MBA / Management", "Graduate",
  "Chartered Accountant CA", "Advocate / Law", "HSC / 12th", "Other"
)

val filterComplexionOptions = listOf(
  "Very Fair", "Fair", "Wheatish / Medium", "Wheatish Brown", "Dusky"
)

@Composable
fun ActiveFilterChip(
  text: String,
  onDismiss: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(GoldPrimary.copy(alpha = 0.18f))
      .border(1.dp, GoldLight.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
      .padding(start = 10.dp, end = 6.dp, top = 3.dp, bottom = 3.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = text,
        fontSize = 10.5.sp,
        fontWeight = FontWeight.Bold,
        color = GoldLight
      )
      Spacer(modifier = Modifier.width(4.dp))
      Box(
        modifier = Modifier
          .size(16.dp)
          .clip(CircleShape)
          .background(Color.Black.copy(alpha = 0.6f))
          .clickable { onDismiss() },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Remove filter",
          tint = GoldLight,
          modifier = Modifier.size(10.dp)
        )
      }
    }
  }
}

@Composable
fun CategoryFilterButton(
  label: String,
  count: Int,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(48.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(
        if (isSelected) Brush.horizontalGradient(listOf(GoldLight, GoldMedium, GoldDark))
        else Brush.horizontalGradient(listOf(Color(0xFF0F110F), Color(0xFF161916)))
      )
      .border(
        width = if (isSelected) 1.5.dp else 1.dp,
        color = if (isSelected) GoldLight else GoldLight.copy(alpha = 0.45f),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 14.dp, vertical = 2.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = label,
        fontFamily = FontFamily.SansSerif,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
        fontSize = 11.5.sp,
        color = if (isSelected) Color(0xFF141512) else TextIvory,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = "$count ${if (count == 1) "profile" else "profiles"}",
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 9.5.sp,
        color = if (isSelected) Color(0xFF141512).copy(alpha = 0.85f) else GoldLight
      )
    }
  }
}

@Composable
fun RegistrationFilterButton(
  label: String,
  selectedValue: String?,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isActive: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(42.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(
        if (isActive) Brush.horizontalGradient(listOf(GoldLight, GoldMedium, GoldDark))
        else Brush.horizontalGradient(listOf(Color(0xFF0F110F), Color(0xFF161916)))
      )
      .border(
        width = if (isActive) 1.5.dp else 1.dp,
        color = if (isActive) GoldLight else GoldLight.copy(alpha = 0.45f),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable { onClick() }
      .padding(horizontal = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isActive) Color(0xFF141512) else GoldPrimary,
        modifier = Modifier.size(15.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = if (selectedValue.isNullOrBlank()) label else "$label: $selectedValue",
        fontFamily = FontFamily.SansSerif,
        fontWeight = if (isActive) FontWeight.Bold else FontWeight.SemiBold,
        fontSize = 11.5.sp,
        color = if (isActive) Color(0xFF141512) else TextIvory,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Spacer(modifier = Modifier.width(4.dp))
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "Expand",
        tint = if (isActive) Color(0xFF141512) else GoldLight.copy(alpha = 0.7f),
        modifier = Modifier.size(14.dp)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeRangeFilterBottomSheet(
  currentMinAge: Int?,
  currentMaxAge: Int?,
  onApply: (min: Int?, max: Int?) -> Unit,
  onClear: () -> Unit,
  onDismiss: () -> Unit
) {
  var minText by remember { mutableStateOf(currentMinAge?.toString() ?: "") }
  var maxText by remember { mutableStateOf(currentMaxAge?.toString() ?: "") }
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
        .padding(bottom = 36.dp, start = 20.dp, end = 20.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Cake, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Filter by Age Range",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = GoldLight
          )
        }
        TextButton(onClick = onClear) {
          Text("Reset", color = TextSand, fontSize = 12.sp)
        }
      }

      Text(
        text = "Enter custom age range (e.g. 25 to 30) or choose a preset:",
        fontSize = 11.5.sp,
        color = TextSand
      )

      // Quick presets (e.g. 25-30 explicitly provided as requested)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(
          "18-24" to (18 to 24),
          "25-30" to (25 to 30),
          "30-35" to (30 to 35),
          "35-40" to (35 to 40)
        ).forEach { (label, range) ->
          val isSelected = minText == range.first.toString() && maxText == range.second.toString()
          Box(
            modifier = Modifier
              .weight(1f)
              .height(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) GoldPrimary else SurfaceDark)
              .border(1.dp, if (isSelected) GoldLight else GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
              .clickable {
                minText = range.first.toString()
                maxText = range.second.toString()
              },
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = label,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) TextDark else TextIvory
            )
          }
        }
      }

      // Custom Inputs Row: Min to Max
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = minText,
          onValueChange = { minText = it.filter { ch -> ch.isDigit() }.take(2) },
          label = { Text("Min Age", color = GoldLight, fontSize = 11.sp) },
          placeholder = { Text("e.g. 25", color = Color.Gray, fontSize = 11.sp) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldLight,
            unfocusedBorderColor = GoldPrimary.copy(alpha = 0.5f),
            focusedTextColor = TextIvory,
            unfocusedTextColor = TextIvory
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        )

        Text("to", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldLight)

        OutlinedTextField(
          value = maxText,
          onValueChange = { maxText = it.filter { ch -> ch.isDigit() }.take(2) },
          label = { Text("Max Age", color = GoldLight, fontSize = 11.sp) },
          placeholder = { Text("e.g. 30", color = Color.Gray, fontSize = 11.sp) },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldLight,
            unfocusedBorderColor = GoldPrimary.copy(alpha = 0.5f),
            focusedTextColor = TextIvory,
            unfocusedTextColor = TextIvory
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.weight(1f)
        )
      }

      // Apply Button
      Button(
        onClick = {
          val min = minText.toIntOrNull()
          val max = maxText.toIntOrNull()
          onApply(min, max)
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = TextDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(46.dp)
      ) {
        Text("Apply Age Filter", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeightRangeFilterBottomSheet(
  currentMinInches: Int?,
  currentMaxInches: Int?,
  onApply: (min: Int?, max: Int?) -> Unit,
  onClear: () -> Unit,
  onDismiss: () -> Unit
) {
  var selectedMinInches by remember { mutableStateOf(currentMinInches ?: 58) }
  var selectedMaxInches by remember { mutableStateOf(currentMaxInches ?: 74) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val heightOptionsList = listOf(
    58 to "4'10\"",
    59 to "4'11\"",
    60 to "5'0\"",
    61 to "5'1\"",
    62 to "5'2\"",
    63 to "5'3\"",
    64 to "5'4\"",
    65 to "5'5\"",
    66 to "5'6\"",
    67 to "5'7\"",
    68 to "5'8\"",
    69 to "5'9\"",
    70 to "5'10\"",
    71 to "5'11\"",
    72 to "6'0\"",
    73 to "6'1\"",
    74 to "6'2\"",
    75 to "6'3\"",
    76 to "6'4\""
  )

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color(0xFF0F110F),
    contentColor = TextIvory
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 36.dp, start = 20.dp, end = 20.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Height, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Filter by Height Range",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = GoldLight
          )
        }
        TextButton(onClick = onClear) {
          Text("Reset", color = TextSand, fontSize = 12.sp)
        }
      }

      Text(
        text = "Select custom height criteria (e.g. 5'0\" to 5'8\"): ",
        fontSize = 11.5.sp,
        color = TextSand
      )

      // Quick height presets
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        listOf(
          "5'0\"-5'4\"" to (60 to 64),
          "5'2\"-5'6\"" to (62 to 66),
          "5'4\"-5'8\"" to (64 to 68),
          "5'8\"-6'0\"" to (68 to 72)
        ).forEach { (label, range) ->
          val isSelected = selectedMinInches == range.first && selectedMaxInches == range.second
          Box(
            modifier = Modifier
              .weight(1f)
              .height(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) GoldPrimary else SurfaceDark)
              .border(1.dp, if (isSelected) GoldLight else GoldPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
              .clickable {
                selectedMinInches = range.first
                selectedMaxInches = range.second
              },
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = label,
              fontSize = 10.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) TextDark else TextIvory
            )
          }
        }
      }

      // Height Pickers Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Min Height Picker Box
        Column(modifier = Modifier.weight(1f)) {
          Text("Min Height", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.height(4.dp))
          var showMinDropdown by remember { mutableStateOf(false) }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceDark)
              .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .clickable { showMinDropdown = !showMinDropdown }
              .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(formatInchesToHeight(selectedMinInches), fontSize = 13.sp, color = TextIvory, fontWeight = FontWeight.Bold)
              Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
            }
          }
          if (showMinDropdown) {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(140.dp).background(SurfaceDark)) {
              items(heightOptionsList.filter { it.first <= selectedMaxInches }) { (inches, label) ->
                Text(
                  text = label,
                  fontSize = 12.sp,
                  color = if (inches == selectedMinInches) GoldLight else TextIvory,
                  modifier = Modifier.fillMaxWidth().clickable {
                    selectedMinInches = inches
                    showMinDropdown = false
                  }.padding(8.dp)
                )
              }
            }
          }
        }

        Text("to", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GoldLight)

        // Max Height Picker Box
        Column(modifier = Modifier.weight(1f)) {
          Text("Max Height", fontSize = 11.sp, color = GoldLight, fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.height(4.dp))
          var showMaxDropdown by remember { mutableStateOf(false) }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceDark)
              .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
              .clickable { showMaxDropdown = !showMaxDropdown }
              .padding(horizontal = 10.dp),
            contentAlignment = Alignment.CenterStart
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(formatInchesToHeight(selectedMaxInches), fontSize = 13.sp, color = TextIvory, fontWeight = FontWeight.Bold)
              Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
            }
          }
          if (showMaxDropdown) {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(140.dp).background(SurfaceDark)) {
              items(heightOptionsList.filter { it.first >= selectedMinInches }) { (inches, label) ->
                Text(
                  text = label,
                  fontSize = 12.sp,
                  color = if (inches == selectedMaxInches) GoldLight else TextIvory,
                  modifier = Modifier.fillMaxWidth().clickable {
                    selectedMaxInches = inches
                    showMaxDropdown = false
                  }.padding(8.dp)
                )
              }
            }
          }
        }
      }

      // Apply Button
      Button(
        onClick = {
          onApply(selectedMinInches, selectedMaxInches)
        },
        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = TextDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(46.dp)
      ) {
        Text("Apply Height Filter", fontWeight = FontWeight.Bold, fontSize = 13.sp)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSelectFilterBottomSheet(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selectedOption: String?,
  options: List<String>,
  onSelect: (String?) -> Unit,
  onDismiss: () -> Unit
) {
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
        .padding(bottom = 32.dp, start = 20.dp, end = 20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(icon, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Select $title",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = GoldLight
          )
        }
        TextButton(onClick = { onSelect(null) }) {
          Text("Clear", color = TextSand, fontSize = 12.sp)
        }
      }

      HorizontalDivider(color = GoldPrimary.copy(alpha = 0.2f), modifier = Modifier.padding(vertical = 10.dp))

      LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 380.dp)) {
        item {
          val isAllSelected = selectedOption == null
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelect(null) }
              .padding(vertical = 12.dp, horizontal = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("All $title (Any)", fontSize = 13.5.sp, color = if (isAllSelected) GoldLight else TextIvory, fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal)
              if (isAllSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
              }
            }
          }
        }

        items(options) { opt ->
          val isSelected = selectedOption == opt
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onSelect(opt) }
              .padding(vertical = 12.dp, horizontal = 8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(opt, fontSize = 13.5.sp, color = if (isSelected) GoldLight else TextIvory, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
              if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }
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
  isSelected: Boolean = false,
  isRepeat: Boolean = false,
  repeatModeAllowed: Boolean = false,
  onToggleSelect: () -> Unit = {},
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDulhan = dossier.gender.equals("Dulhan", ignoreCase = true)
  val context = LocalContext.current

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) Color(0xFF1B221B) else if (isDulhan) Color(0xFF0B1711).copy(alpha = 0.92f) else Color(0xFF131512).copy(alpha = 0.92f)
    ),
    border = androidx.compose.foundation.BorderStroke(
      if (isSelected) 1.5.dp else 1.dp,
      if (isSelected) GoldLight else if (isRepeat && !repeatModeAllowed) Color(0xFFFF9E9E).copy(alpha = 0.7f) else if (isDulhan) EmeraldBorder.copy(alpha = 0.7f) else GoldPrimary.copy(alpha = 0.35f)
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
      // Selection Tick Mark Circle
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(if (isSelected) GoldPrimary else Color(0xFF181E18))
          .border(
            1.5.dp,
            if (isSelected) GoldLight else if (isRepeat && !repeatModeAllowed) Color(0xFFFF8B8B) else GoldLight.copy(alpha = 0.45f),
            CircleShape
          )
          .clickable { onToggleSelect() }
          .testTag("select_dossier_${dossier.dossierCode}"),
        contentAlignment = Alignment.Center
      ) {
        if (isSelected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Selected",
            tint = TextDark,
            modifier = Modifier.size(19.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

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
          alignment = Alignment.TopCenter,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Content Column
      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        // Dossier Code, Repeat Badge & Gender Badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = dossier.dossierCode,
              fontFamily = FontFamily.Serif,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = GoldLight
            )

            if (isRepeat) {
              Spacer(modifier = Modifier.width(5.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (repeatModeAllowed) Color(0xFF352B09) else Color(0xFF331616))
                  .border(
                    1.dp,
                    if (repeatModeAllowed) GoldPrimary else Color(0xFFFF7070),
                    RoundedCornerShape(6.dp)
                  )
                  .padding(horizontal = 5.dp, vertical = 2.dp)
              ) {
                Text(
                  text = if (repeatModeAllowed) "🔄 Repeat" else "⚠️ Pehle Bheja Gaya",
                  fontSize = 8.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (repeatModeAllowed) GoldLight else Color(0xFFFFB4B4)
                )
              }
            }
          }

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
              color = GoldLight
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

// Category Filter Data Model
data class CategoryFilterItem(
  val label: String,
  val filterKey: String,
  val count: Int
)

// Proceed and Send to WhatsApp Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProceedShareDialog(
  initialPhoneNumber: String = "",
  selectedProfiles: List<CandidateDossier>,
  initialRepeatModeAllowed: Boolean = false,
  isProfileRepeat: (CandidateDossier) -> Boolean = { false },
  onToggleProfile: (Long) -> Unit,
  onDismiss: () -> Unit,
  onSendWhatsApp: (String, List<CandidateDossier>) -> Unit
) {
  val context = LocalContext.current
  var phoneNumber by remember(initialPhoneNumber) { mutableStateOf(initialPhoneNumber) }
  var phoneError by remember { mutableStateOf(false) }
  var repeatModeAllowed by remember(initialRepeatModeAllowed) { mutableStateOf(initialRepeatModeAllowed) }

  val sentCodes = remember(phoneNumber) {
    SentProfilesHistoryManager.getSentProfileCodes(context, phoneNumber)
  }

  fun checkRepeat(profile: CandidateDossier): Boolean {
    return isProfileRepeat(profile) || SentProfilesHistoryManager.isProfileCodeRepeat(sentCodes, profile.dossierCode)
  }

  val activeProfilesToSend = remember(selectedProfiles, repeatModeAllowed, sentCodes) {
    if (repeatModeAllowed) {
      selectedProfiles
    } else {
      selectedProfiles.filterNot { checkRepeat(it) }
    }
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = Color(0xFF101411),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)
        .padding(bottom = 32.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Send Profiles via WhatsApp",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = GoldLight
          )
          Text(
            text = "Khan Marriage Bureau • ${selectedProfiles.size} Profiles Selected",
            fontSize = 11.5.sp,
            color = TextSand
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = GoldLight)
        }
      }

      HorizontalDivider(color = GoldPrimary.copy(alpha = 0.25f))

      // WhatsApp Mobile Number Input Field
      Column {
        Text(
          text = "Registered WhatsApp Mobile Number:",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = TextSand
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
          value = phoneNumber,
          onValueChange = {
            phoneNumber = it
            phoneError = false
          },
          placeholder = { Text("e.g. 9812345678 or +91 9812345678", color = TextMuted, fontSize = 12.5.sp) },
          leadingIcon = {
            Icon(Icons.Default.Phone, contentDescription = null, tint = GoldLight, modifier = Modifier.size(18.dp))
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GoldLight,
            unfocusedBorderColor = GoldPrimary.copy(alpha = 0.45f),
            focusedTextColor = TextIvory,
            unfocusedTextColor = TextIvory,
            cursorColor = GoldPrimary
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_whatsapp_phone")
        )
        if (phoneError) {
          Text(
            text = "Please enter a valid mobile number",
            fontSize = 11.sp,
            color = Color(0xFFFF8B8B),
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
          )
        }
      }

      // History summary for this number
      if (phoneNumber.trim().length >= 7) {
        if (sentCodes.isNotEmpty()) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1F17)),
            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text(
                text = "📋 Abhi tak bheje gaye: ${sentCodes.size} Profiles (${sentCodes.joinToString(", ") { "#$it" }})",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = GoldLight
              )
            }
          }
        } else {
          Text(
            text = "✨ Naya Number: Pehle 0 profiles bheje gaye hain",
            fontSize = 11.sp,
            color = EmeraldLight
          )
        }
      }

      // Dispatch Mode Toggle (New vs Repeat)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF0D120E))
          .border(1.dp, GoldPrimary.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (!repeatModeAllowed) GoldPrimary else Color.Transparent)
            .clickable { repeatModeAllowed = false },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "✨ Naye Profiles (New Only)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (!repeatModeAllowed) TextDark else TextSand
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (repeatModeAllowed) Color(0xFFD4AF37) else Color.Transparent)
            .clickable { repeatModeAllowed = true },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "🔄 Repeat Bhejein (Allow All)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (repeatModeAllowed) TextDark else TextSand
          )
        }
      }

      // Selected Profiles List with select/unselect capability
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Selected Profiles (${selectedProfiles.size}):",
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Bold,
          color = GoldLight
        )
        Text(
          text = if (!repeatModeAllowed) "Sirf naye profiles select honge" else "Tap checkbox to toggle",
          fontSize = 10.5.sp,
          color = TextSand
        )
      }

      if (selectedProfiles.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No profiles currently selected. Select profiles from list.",
            color = TextSand,
            fontSize = 12.sp
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 210.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(selectedProfiles, key = { it.id }) { profile ->
            val isRepeat = checkRepeat(profile)
            val isIncluded = repeatModeAllowed || !isRepeat

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isIncluded) Color(0xFF171D17) else Color(0xFF1C1313))
                .border(
                  1.dp,
                  if (!isIncluded) Color(0xFFFF6B6B).copy(alpha = 0.5f) else GoldPrimary.copy(alpha = 0.35f),
                  RoundedCornerShape(12.dp)
                )
                .clickable {
                  if (isIncluded) {
                    onToggleProfile(profile.id)
                  } else {
                    repeatModeAllowed = true
                  }
                }
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Checkbox
                Box(
                  modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(if (isIncluded) GoldPrimary else Color(0xFF3A2020)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = if (isIncluded) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isIncluded) TextDark else Color(0xFFFF9E9E),
                    modifier = Modifier.size(16.dp)
                  )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = profile.dossierCode,
                      fontWeight = FontWeight.Bold,
                      fontSize = 12.sp,
                      color = GoldLight
                    )
                    if (isRepeat) {
                      Spacer(modifier = Modifier.width(5.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(if (isIncluded) Color(0xFF352B09) else Color(0xFF351616))
                          .border(1.dp, if (isIncluded) GoldPrimary else Color(0xFFFF6B6B), RoundedCornerShape(4.dp))
                          .padding(horizontal = 4.dp, vertical = 1.dp)
                      ) {
                        Text(
                          text = if (isIncluded) "🔄 Repeat" else "⚠️ Pehle Bheja Gaya (Excluded)",
                          fontSize = 8.5.sp,
                          fontWeight = FontWeight.Bold,
                          color = if (isIncluded) GoldLight else Color(0xFFFFB4B4)
                        )
                      }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = profile.candidateName,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 12.5.sp,
                      color = TextIvory
                    )
                  }
                  Text(
                    text = "${profile.gender} • ${profile.age} Yrs • ${profile.city} • ${profile.occupation}",
                    fontSize = 10.sp,
                    color = TextSand,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }
      }

      // Summary Count Badge & WhatsApp Send Button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color(0xFF1C221C))
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (repeatModeAllowed) "Total to Send (All):" else "Total to Send (New Only):",
          fontSize = 11.5.sp,
          color = TextSand
        )
        Text(
          "${activeProfilesToSend.size} Profiles (${activeProfilesToSend.joinToString(", ") { it.dossierCode }})",
          fontSize = 11.5.sp,
          fontWeight = FontWeight.Bold,
          color = GoldLight
        )
      }

      Button(
        onClick = {
          if (phoneNumber.trim().length < 7) {
            phoneError = true
          } else if (activeProfilesToSend.isEmpty()) {
            Toast.makeText(context, "Koi naya profile select nahi hai. Repeat allow karne ke liye 'Repeat Bhejein' mode chunein.", Toast.LENGTH_LONG).show()
          } else {
            onSendWhatsApp(phoneNumber.trim(), activeProfilesToSend)
          }
        },
        enabled = activeProfilesToSend.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF25D366),
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("button_send_whatsapp")
      ) {
        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(17.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Send ${activeProfilesToSend.size} Profiles on WhatsApp",
          fontWeight = FontWeight.Bold,
          fontSize = 13.5.sp
        )
      }
    }
  }
}

// WhatsApp Dispatch Intent Function
fun sendProfilesViaWhatsApp(
  context: Context,
  phoneNumber: String,
  profiles: List<CandidateDossier>
) {
  if (profiles.isEmpty()) return

  val cleanPhone = phoneNumber.replace(Regex("[^0-9]"), "")
  val targetPhone = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone

  val sb = StringBuilder()
  sb.append("✨ *KHAN MARRIAGE BUREAU* ✨\n")
  sb.append("Shortlisted Candidate Profiles\n\n")
  sb.append("📋 *Total Profiles:* ${profiles.size}\n")
  sb.append("🔢 *Profile Codes:* ${profiles.joinToString(", ") { it.dossierCode }}\n")
  sb.append("━━━━━━━━━━━━━━━━━━━━\n\n")

  profiles.forEachIndexed { index, p ->
    sb.append("${index + 1}️⃣ *Profile No: ${p.dossierCode}*\n")
    sb.append("• *Name:* ${p.candidateName} (${p.gender})\n")
    sb.append("• *Age & Height:* ${p.age} Yrs • ${p.height}\n")
    sb.append("• *City:* ${p.city}\n")
    sb.append("• *Profession:* ${p.occupation}\n")
    sb.append("• *Education:* ${p.education}\n")
    sb.append("• *Marital Status:* ${p.maritalStatus}\n")
    if (p.complexion.isNotBlank()) {
      sb.append("• *Complexion:* ${p.complexion}\n")
    }
    sb.append("\n")
  }

  sb.append("━━━━━━━━━━━━━━━━━━━━\n")
  sb.append("Kindly reply with the profile numbers you would like to proceed with.")

  val encodedMsg = Uri.encode(sb.toString())
  val uriString = if (targetPhone.isNotBlank()) {
    "https://api.whatsapp.com/send?phone=$targetPhone&text=$encodedMsg"
  } else {
    "https://api.whatsapp.com/send?text=$encodedMsg"
  }

  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
  try {
    context.startActivity(intent)
  } catch (e: Exception) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, sb.toString())
    }
    context.startActivity(Intent.createChooser(shareIntent, "Send Profiles via"))
  }
}
