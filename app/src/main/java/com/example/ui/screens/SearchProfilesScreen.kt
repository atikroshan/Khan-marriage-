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
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
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

  var showAgeFilterDialog by remember { mutableStateOf(false) }
  var showHeightFilterDialog by remember { mutableStateOf(false) }
  var showCityFilterDialog by remember { mutableStateOf(false) }
  var showMaritalFilterDialog by remember { mutableStateOf(false) }
  var showOccupationFilterDialog by remember { mutableStateOf(false) }
  var showEducationFilterDialog by remember { mutableStateOf(false) }
  var showComplexionFilterDialog by remember { mutableStateOf(false) }

  var selectedDossierForDetail by remember { mutableStateOf<CandidateDossier?>(null) }
  var showDeleteDialog by remember { mutableStateOf(false) }
  var showSheetSettingsDialog by remember { mutableStateOf(false) }

  val hasActiveFilters = minAge != null || maxAge != null || minHeightInches != null || maxHeightInches != null ||
      selectedCityFilter != null || selectedMaritalStatusFilter != null || selectedOccupationFilter != null ||
      selectedEducationFilter != null || selectedComplexionFilter != null

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
      else dossier.occupation.contains(selectedOccupationFilter!!, ignoreCase = true)

    val matchesEducation = if (selectedEducationFilter == null || selectedEducationFilter == "All") true 
      else dossier.education.contains(selectedEducationFilter!!, ignoreCase = true)

    val matchesComplexion = if (selectedComplexionFilter == null || selectedComplexionFilter == "All") true 
      else dossier.complexion.contains(selectedComplexionFilter!!, ignoreCase = true)

    val matchesQuery = if (searchQuery.isBlank()) true else {
      dossier.candidateName.contains(searchQuery, ignoreCase = true) ||
        dossier.dossierCode.contains(searchQuery, ignoreCase = true) ||
        dossier.city.contains(searchQuery, ignoreCase = true) ||
        dossier.occupation.contains(searchQuery, ignoreCase = true) ||
        dossier.education.contains(searchQuery, ignoreCase = true)
    }

    matchesGender && matchesAge && matchesHeight && matchesCity && matchesMarital &&
      matchesOccupation && matchesEducation && matchesComplexion && matchesQuery
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
      // Top Bar with Prominent Back Button (Edge-to-edge statusBarsPadding)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
          .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Regular Circular Back Button with Arrow
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0xFF141714))
            .border(1.dp, GoldPrimary, CircleShape)
            .clickable { onBack() }
            .testTag("search_back_button"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = GoldLight,
            modifier = Modifier.size(20.dp)
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

        // Gender Avatar (Moved to Right Side)
        if (initialGender != "All") {
          Spacer(modifier = Modifier.width(8.dp))
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .border(1.dp, GoldPrimary, CircleShape)
              .background(SurfaceDark)
          ) {
            AsyncImage(
              model = ImageRequest.Builder(LocalContext.current)
                .data(if (initialGender.equals("Dulhan", true)) AppDatabase.DULHAN_IMAGE_URL else AppDatabase.DULHA_IMAGE_URL)
                .crossfade(true)
                .build(),
              contentDescription = "Gender Avatar",
              contentScale = ContentScale.Crop,
              alignment = Alignment.TopCenter,
              modifier = Modifier.fillMaxSize()
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Placeholder to maintain spacing if needed
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
              .background(GoldPrimary.copy(alpha = 0.8f))
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (searchQuery.isNotBlank()) "Searching \"$searchQuery\"" else "Exploring ${if (initialGender != "All") initialGender else if (selectedGenderTab == "All") "All" else selectedGenderTab} Matches",
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
      if (initialGender == "All") {
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
  "Solapur", "Mumbai", "Pune", "Akkalkot", "Pandharpur", "Sangli", "Satara", "Osmanabad", "Gulbarga", "Hyderabad"
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
