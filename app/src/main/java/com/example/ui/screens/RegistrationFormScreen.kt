package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppDatabase
import com.example.data.CandidateDossier
import com.example.ui.components.DossierDropdownField
import com.example.ui.components.HeightFeetInchesPicker
import com.example.ui.components.JewelDivider
import com.example.ui.components.KhanHeader
import com.example.ui.components.PhotoUploadSection
import com.example.ui.theme.EmeraldBorder
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.GoldBorder
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
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun RegistrationFormScreen(
  gender: String, // "Dulhan" or "Dulha"
  onSubmitSuccess: (newDossier: CandidateDossier) -> Unit,
  onNavigateToSearch: () -> Unit,
  onBack: () -> Unit,
  onSaveDossier: suspend (CandidateDossier) -> Long,
  modifier: Modifier = Modifier
) {
  BackHandler { onBack() }
  val context = LocalContext.current
  val scope = rememberCoroutineScope()

  // Form State
  var selectedCity by remember { mutableStateOf("") }
  var customCityName by remember { mutableStateOf("") }
  var selectedAge by remember { mutableStateOf("") }
  var selectedMaritalStatus by remember { mutableStateOf("") }
  var selectedOccupation by remember { mutableStateOf("") }
  var customOccupationName by remember { mutableStateOf("") }
  var selectedEducation by remember { mutableStateOf("") }
  var customEducationName by remember { mutableStateOf("") }
  var selectedComplexion by remember { mutableStateOf("Fair") }
  var selectedFeet by remember { mutableStateOf("5") }
  var selectedInches by remember { mutableStateOf("6") }

  // Additional detail fields
  var candidateName by remember { mutableStateOf("") }
  var contactPhone by remember { mutableStateOf("") }
  var casteSect by remember { mutableStateOf("Sunni / Muslim") }
  var familyDetails by remember { mutableStateOf("") }

  // Photos
  val defaultPortrait = if (gender == "Dulhan") AppDatabase.DULHAN_IMAGE_URL else AppDatabase.DULHA_IMAGE_URL
  var image1Uri by remember { mutableStateOf<String?>(defaultPortrait) }
  var image2Uri by remember { mutableStateOf<String?>(defaultPortrait) }

  var isSubmitting by remember { mutableStateOf(false) }
  var showSuccessDialog by remember { mutableStateOf(false) }
  var createdDossierCode by remember { mutableStateOf("") }
  var lastSyncResult by remember { mutableStateOf<com.example.data.GoogleSheetsDriveService.SyncResult?>(null) }
  var showExtraDetails by remember { mutableStateOf(false) }

  // City Options as requested
  val cityOptions = listOf(
    "Solapur",
    "Mumbai",
    "Pune",
    "Akkalkot",
    "Pandharpur",
    "Sangli",
    "Satara",
    "Osmanabad",
    "Gulbarga",
    "Hyderabad",
    "Other"
  )

  // Age Options (18 to 55)
  val ageOptions = (18..55).map { "$it Years" }

  // Marital Status Options
  val maritalOptions = listOf(
    "Never Married",
    "Divorced",
    "Widowed",
    "Khula / Separated",
    "Second Marriage"
  )

  // Occupation Options (Dynamic: Businessman / Businesswoman, Homemaker only for Dulhan)
  val isDulhan = gender.equals("Dulhan", ignoreCase = true)
  val blackTextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldLight,
    unfocusedBorderColor = GoldLight.copy(alpha = 0.5f),
    focusedTextColor = Color(0xFFFFFFFF),
    unfocusedTextColor = Color(0xFFFFFFFF),
    focusedLabelColor = GoldLight,
    unfocusedLabelColor = Color(0xFFCCCCCC),
    focusedPlaceholderColor = Color(0xFF888888),
    unfocusedPlaceholderColor = Color(0xFF888888),
    focusedContainerColor = Color(0xFF0F110F),
    unfocusedContainerColor = Color(0xFF0F110F),
    focusedLeadingIconColor = GoldLight,
    unfocusedLeadingIconColor = GoldLight.copy(alpha = 0.8f),
    cursorColor = GoldLight
  )
  val occupationOptions = buildList {
    add(if (isDulhan) "Businesswoman" else "Businessman")
    add("Doctor")
    add("Engineer")
    add("Software Professional / IT Engineer")
    add("Software Developer / Programmer")
    add("Data Scientist / Analyst")
    add("Civil Engineer / Architect")
    add("Chartered Accountant / Company Secretary")
    add("Finance / Banking Professional")
    add("Business Owner / Entrepreneur")
    add("Corporate Executive / Manager")
    add("Marketing / Sales Professional")
    add("HR / Administration Professional")
    add("Teacher / Professor / Lecturer")
    add("Researcher / Scientist")
    add("Lawyer / Advocate")
    add("Government / Public Service Employee")
    add("Defense / Police Personnel")
    add("Medical Practitioner / Nurse / Pharmacist")
    add("Artist / Designer / Media Professional")
    add("Freelancer / Consultant")
    add("Student")
    if (isDulhan) {
      add("Homemaker")
    }
    add("Other")
  }

  // Education Options
  val educationOptions = listOf(
    "Doctor (MBBS, MD, MS, BDS, BAMS, BHMS)",
    "Engineer (B.E., B.Tech)",
    "Post-Graduate Engineer (M.E., M.Tech)",
    "Advocate",
    "Master of Laws (LL.M.)",
    "Chartered Accountant CA",
    "BBA / Management Graduate",
    "MBA / PGDM / Management Post-Graduate",
    "BCA / B.Sc. IT",
    "MCA / M.Sc. IT",
    "B.Sc. (Bachelor of Science)",
    "M.Sc. (Master of Science)",
    "B.A. (Bachelor of Arts)",
    "M.A. (Master of Arts)",
    "B.Com. (Bachelor of Commerce)",
    "M.Com. (Master of Commerce)",
    "Architect / Designer (B.Arch, B.Des)",
    "Teacher / Education (B.Ed., M.Ed.)",
    "Ph.D. / Doctorate",
    "Diploma / Polytechnic",
    "Graduate (Other)",
    "Post-Graduate (Other)",
    "HSC / 12th",
    "SSC / 10th",
    "8th",
    "Un educated",
    "Other"
  )

  // Complexion Options
  val complexionOptions = listOf(
    "Very Fair",
    "Fair",
    "Wheatish / Medium",
    "Wheatish Brown",
    "Dusky"
  )

  val avatarUrl = if (gender == "Dulhan") AppDatabase.DULHAN_IMAGE_URL else AppDatabase.DULHA_IMAGE_URL
  val formSubtitle = "CANDIDATE DOSSIER FORM (${gender.uppercase()})"

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    // Top Header with Back button and Bureau branding (Edge-to-edge)
    KhanHeader(
      badgeTitle = "NEW REGISTRATION",
      badgeSubtitle = formSubtitle,
      avatarUrl = avatarUrl,
      onAvatarClick = { onBack() },
      onBack = onBack,
      modifier = Modifier.fillMaxWidth()
    )

    // Scrollable Form Body
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. City / Location Dropdown
      DossierDropdownField(
        label = "City / Location",
        icon = Icons.Default.LocationCity,
        selectedValue = selectedCity,
        options = cityOptions,
        placeholder = "Select City",
        testTag = "dropdown_city",
        onValueChange = { selectedCity = it }
      )

      // Custom City text field when "Other" is selected
      if (selectedCity == "Other") {
        OutlinedTextField(
          value = customCityName,
          onValueChange = { customCityName = it },
          label = { Text("Enter City / Town Name", color = GoldLight) },
          placeholder = { Text("e.g. Kolhapur, Latur, Beed...", color = Color(0xFF888888)) },
          leadingIcon = {
            Icon(Icons.Default.LocationCity, contentDescription = null, tint = GoldLight)
          },
          colors = blackTextFieldColors,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_custom_city")
        )
      }

      // 2. Age
      DossierDropdownField(
        label = "Age",
        icon = Icons.Default.Cake,
        selectedValue = selectedAge,
        options = ageOptions,
        placeholder = "Select Age",
        testTag = "dropdown_age",
        onValueChange = { selectedAge = it }
      )

      // 3. Marital Status
      DossierDropdownField(
        label = "Marital Status",
        icon = Icons.Default.FavoriteBorder,
        selectedValue = selectedMaritalStatus,
        options = maritalOptions,
        placeholder = "Select Marital Status",
        testTag = "dropdown_marital_status",
        onValueChange = { selectedMaritalStatus = it }
      )

      // 4. Occupation
      DossierDropdownField(
        label = "Occupation",
        icon = Icons.Default.WorkOutline,
        selectedValue = selectedOccupation,
        options = occupationOptions,
        placeholder = "Select Occupation",
        testTag = "dropdown_occupation",
        onValueChange = { selectedOccupation = it }
      )

      // Custom Occupation text field when "Other" is selected
      if (selectedOccupation == "Other") {
        OutlinedTextField(
          value = customOccupationName,
          onValueChange = { customOccupationName = it },
          label = { Text("Enter Occupation Title", color = GoldLight) },
          placeholder = { Text("e.g. Pilot, Journalist, Event Planner...", color = Color(0xFF888888)) },
          leadingIcon = {
            Icon(Icons.Default.WorkOutline, contentDescription = null, tint = GoldLight)
          },
          colors = blackTextFieldColors,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_custom_occupation")
        )
      }

      // 5. Education
      DossierDropdownField(
        label = "Education",
        icon = Icons.Default.School,
        selectedValue = selectedEducation,
        options = educationOptions,
        placeholder = "Select Education",
        testTag = "dropdown_education",
        onValueChange = { selectedEducation = it }
      )

      // Custom Education text field when "Other" is selected
      if (selectedEducation == "Other") {
        OutlinedTextField(
          value = customEducationName,
          onValueChange = { customEducationName = it },
          label = { Text("Enter Qualification / Degree", color = GoldLight) },
          placeholder = { Text("e.g. B.Pharm, DMLT, B.Des, B.Voc...", color = Color(0xFF888888)) },
          leadingIcon = {
            Icon(Icons.Default.School, contentDescription = null, tint = GoldLight)
          },
          colors = blackTextFieldColors,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_custom_education")
        )
      }

      // 6. Complexion (Color) Dropdown (below Education)
      DossierDropdownField(
        label = "Complexion (Color)",
        icon = Icons.Default.Palette,
        selectedValue = selectedComplexion,
        options = complexionOptions,
        placeholder = "Select Complexion",
        testTag = "dropdown_complexion",
        onValueChange = { selectedComplexion = it }
      )

      // 7. Height Dropdown (Feet & Inches, formatted as 5'6")
      HeightFeetInchesPicker(
        feet = selectedFeet,
        inches = selectedInches,
        onFeetChange = { selectedFeet = it },
        onInchesChange = { selectedInches = it }
      )

      // Toggle for additional Candidate Bio & Contact Info
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { showExtraDetails = !showExtraDetails }
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (showExtraDetails) "- Hide Details (Name & Contact)" else "+ Add Candidate Name & Contact Details (Optional)",
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp,
          color = GoldLight
        )
      }

      AnimatedVisibility(visible = showExtraDetails) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          // Candidate Name
          OutlinedTextField(
            value = candidateName,
            onValueChange = { candidateName = it },
            label = { Text("Candidate Full Name", color = GoldLight) },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = null, tint = GoldLight)
            },
            colors = blackTextFieldColors,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_candidate_name")
          )

          // Contact Phone / WhatsApp
          OutlinedTextField(
            value = contactPhone,
            onValueChange = { contactPhone = it },
            label = { Text("WhatsApp / Contact Phone", color = GoldLight) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = {
              Icon(Icons.Default.Phone, contentDescription = null, tint = GoldLight)
            },
            colors = blackTextFieldColors,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_contact_phone")
          )
        }
      }

      // 8. Upload Photos Section (Image 1 Front Portrait & Full Length)
      PhotoUploadSection(
        image1Uri = image1Uri,
        image2Uri = image2Uri,
        onImage1Selected = { image1Uri = it },
        onImage2Selected = { image2Uri = it },
        modifier = Modifier.padding(top = 4.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 9. Submit Registration Button (Gold Gradient Pill)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("submit_registration_button")
          .clip(RoundedCornerShape(50))
          .background(
            brush = Brush.horizontalGradient(
              colors = listOf(GoldLight, GoldMedium, GoldDark)
            )
          )
          .clickable(enabled = !isSubmitting) {
            // Validation
            if (selectedCity.isBlank()) {
              Toast.makeText(context, "Please select City", Toast.LENGTH_SHORT).show()
              return@clickable
            }
            if (selectedCity == "Other" && customCityName.isBlank()) {
              Toast.makeText(context, "Please enter your City name", Toast.LENGTH_SHORT).show()
              return@clickable
            }
            if (selectedOccupation == "Other" && customOccupationName.isBlank()) {
              Toast.makeText(context, "Please enter your Occupation title", Toast.LENGTH_SHORT).show()
              return@clickable
            }
            if (selectedEducation == "Other" && customEducationName.isBlank()) {
              Toast.makeText(context, "Please enter your Qualification / Degree name", Toast.LENGTH_SHORT).show()
              return@clickable
            }
            if (selectedAge.isBlank()) {
              Toast.makeText(context, "Please select Age", Toast.LENGTH_SHORT).show()
              return@clickable
            }
            if (selectedMaritalStatus.isBlank()) {
              Toast.makeText(context, "Please select Marital Status", Toast.LENGTH_SHORT).show()
              return@clickable
            }

            val finalCity = if (selectedCity == "Other") customCityName.trim() else selectedCity
            val finalOccupation = if (selectedOccupation == "Other") customOccupationName.trim() else selectedOccupation.ifBlank { "Professional" }
            val finalEducation = if (selectedEducation == "Other") customEducationName.trim() else selectedEducation.ifBlank { "Graduate" }
            val finalHeight = "$selectedFeet'$selectedInches\""

            isSubmitting = true
            scope.launch {
              val db = AppDatabase.getDatabase(context, scope)
              val currentCount = db.dossierDao().getCount()
              val nextSeq = currentCount + 1
              val generatedCode = com.example.data.GoogleSheetsDriveService.formatSerialCode(nextSeq)
              val ageNumber = selectedAge.filter { it.isDigit() }.toIntOrNull() ?: 24

              // 1. Save photo files locally so Coil in SearchProfilesScreen can always render them instantly
              val photoDir = java.io.File(context.filesDir, "candidate_photos").apply { mkdirs() }
              val photo1File = java.io.File(photoDir, "${generatedCode}_1.jpg")
              val photo2File = java.io.File(photoDir, "${generatedCode}_2.jpg")

              val bytes1 = com.example.data.GoogleSheetsDriveService.readImageBytes(
                context = context,
                imageUriString = image1Uri,
                serialCode = generatedCode,
                photoIndex = 1,
                gender = gender
              )
              photo1File.writeBytes(bytes1)

              val bytes2 = com.example.data.GoogleSheetsDriveService.readImageBytes(
                context = context,
                imageUriString = image2Uri ?: image1Uri,
                serialCode = generatedCode,
                photoIndex = 2,
                gender = gender
              )
              photo2File.writeBytes(bytes2)

              val localPhoto1Uri = android.net.Uri.fromFile(photo1File).toString()
              val localPhoto2Uri = android.net.Uri.fromFile(photo2File).toString()

              val newDossier = CandidateDossier(
                dossierCode = generatedCode,
                candidateName = candidateName.ifBlank { "Candidate ${generatedCode.uppercase()}" },
                gender = gender,
                city = finalCity,
                age = ageNumber,
                maritalStatus = selectedMaritalStatus,
                occupation = finalOccupation,
                education = finalEducation,
                complexion = selectedComplexion,
                height = finalHeight,
                casteSect = casteSect,
                familyDetails = familyDetails.ifBlank { "Dignified, verified matrimonial dossier." },
                contactNumber = contactPhone.ifBlank { "+91 98000 00000" },
                frontPortraitUrl = localPhoto1Uri,
                fullLengthUrl = localPhoto2Uri,
                isVerified = true
              )

              // Sync to Google Sheets and Google Drive with photo renaming (kmb001_1.jpg, kmb001_2.jpg)
              val syncResult = com.example.data.GoogleSheetsDriveService.syncRegistration(
                context = context,
                dossier = newDossier,
                sequenceCount = nextSeq,
                image1Uri = localPhoto1Uri,
                image2Uri = localPhoto2Uri
              )

              lastSyncResult = syncResult
              // Save dossier with persistent local photo URI so it renders immediately in Search
              onSaveDossier(newDossier)
              createdDossierCode = generatedCode
              isSubmitting = false
              showSuccessDialog = true
            }
          },
        contentAlignment = Alignment.Center
      ) {
        if (isSubmitting) {
          CircularProgressIndicator(
            color = Color.Black,
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
          )
        } else {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "SUBMIT REGISTRATION",
              fontFamily = FontFamily.SansSerif,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              letterSpacing = 1.sp,
              color = Color(0xFF141512)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = Color(0xFF141512),
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Secondary explicit Back Button
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onBack() }
          .padding(vertical = 8.dp)
          .testTag("form_bottom_back_button"),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = GoldLight,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "GO BACK",
          fontFamily = FontFamily.SansSerif,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          letterSpacing = 1.sp,
          color = GoldLight
        )
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }

  // Success Confirmation Dialog
  if (showSuccessDialog) {
    AlertDialog(
      onDismissRequest = {
        showSuccessDialog = false
        onNavigateToSearch()
      },
      containerColor = Color(0xFF111411),
      titleContentColor = GoldLight,
      textContentColor = TextIvory,
      icon = {
        Icon(
          imageVector = Icons.Default.CheckCircle,
          contentDescription = "Success",
          tint = GoldPrimary,
          modifier = Modifier.size(42.dp)
        )
      },
      title = {
        Text(
          text = "Dossier Registered!",
          fontFamily = FontFamily.Serif,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      },
      text = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Official Bureau Dossier ID (Google Sheets & Drive):",
            fontSize = 12.sp,
            color = TextSand
          )
          Text(
            text = createdDossierCode,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = GoldLight,
            modifier = Modifier.padding(vertical = 4.dp)
          )
          val currentFolder = lastSyncResult?.genderFolder ?: if (isDulhan) "Dulhan (Bride)" else "Dulha (Groom)"
          val currentTab = lastSyncResult?.sheetTabName ?: currentFolder
          Surface(
            color = Color(0xFF1B1F1B),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
              .border(0.5.dp, GoldLight.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
          ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "📁 Drive Folder: $currentFolder / $createdDossierCode",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = GoldLight
              )
              Text(
                text = "🖼️ Photos: ${createdDossierCode}_1.jpg & ${createdDossierCode}_2.jpg",
                fontSize = 11.sp,
                color = TextIvory
              )
              Text(
                text = "📊 Google Sheet Tab: '$currentTab'",
                fontSize = 11.sp,
                color = TextIvory
              )
              Text(
                text = "🔗 Column I (Photo URL): Folder share link saved",
                fontSize = 11.sp,
                color = GoldLight
              )
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showSuccessDialog = false
            onNavigateToSearch()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = TextDark
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("dialog_view_profiles_button")
        ) {
          Text("Explore Verified Matches", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(
          onClick = {
            showSuccessDialog = false
            // Reset fields for another entry
            selectedCity = ""
            customCityName = ""
            selectedAge = ""
            selectedMaritalStatus = ""
            selectedOccupation = ""
            customOccupationName = ""
            selectedEducation = ""
            customEducationName = ""
            candidateName = ""
            contactPhone = ""
          }
        ) {
          Text("Register Another", color = GoldLight)
        }
      }
    )
  }
}
