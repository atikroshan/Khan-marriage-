package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.ui.components.RosePetalsRainOverlay
import com.example.ui.screens.GenderSelectionScreen
import com.example.ui.screens.PortalHomeScreen
import com.example.ui.screens.RegistrationFormScreen
import com.example.ui.screens.SearchProfilesScreen
import com.example.ui.theme.RoyalBlack

@Composable
fun KhanApp(
  viewModel: KhanViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val dossiers by viewModel.dossiers.collectAsStateWithLifecycle()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(RoyalBlack)
  ) {
    // Cinematic falling rose petals rain and background image (shown on Home/GenderSelection, Portal, Registration, and SearchProfiles)
    val showDecor = currentScreen is KhanScreen.GenderSelection ||
        currentScreen is KhanScreen.Portal ||
        currentScreen is KhanScreen.Registration ||
        currentScreen is KhanScreen.SearchProfiles
    
    if (showDecor) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(R.drawable.ic_islamic_jodi_bg)
          .crossfade(true)
          .build(),
        contentDescription = "Islamic Wedding Couple Background",
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxSize()
          .alpha(0.42f)
      )

      // Soft balanced dark gradient overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.verticalGradient(
              0f to RoyalBlack.copy(alpha = 0.35f),
              0.4f to RoyalBlack.copy(alpha = 0.15f),
              0.7f to RoyalBlack.copy(alpha = 0.25f),
              1f to RoyalBlack.copy(alpha = 0.50f)
            )
          )
      )
    }

    AnimatedContent(
      targetState = currentScreen,
      transitionSpec = {
        fadeIn() togetherWith fadeOut()
      },
      label = "ScreenTransition"
    ) { screen ->
      when (screen) {
        is KhanScreen.Portal -> {
          Box(modifier = Modifier.fillMaxSize()) {
            RosePetalsRainOverlay()
            PortalHomeScreen(
              onNavigateToNewRegistration = {
                viewModel.navigateTo(KhanScreen.Registration(viewModel.selectedGenderForRegistration))
              },
              onNavigateToSearchProfiles = {
                viewModel.navigateTo(KhanScreen.SearchProfiles(initialGender = viewModel.selectedGenderForRegistration))
              },
              onBack = {
                viewModel.navigateBack()
              }
            )
          }
        }

        is KhanScreen.GenderSelection -> {
          Box(modifier = Modifier.fillMaxSize()) {
            RosePetalsRainOverlay()
            GenderSelectionScreen(
              modeTitle = screen.modeTitle,
              onSelectGender = { selectedGender ->
                viewModel.selectedGenderForRegistration = selectedGender
                viewModel.navigateTo(KhanScreen.Portal)
              },
              onBack = null
            )
          }
        }

        is KhanScreen.Registration -> {
          Box(modifier = Modifier.fillMaxSize()) {
            RosePetalsRainOverlay()
            RegistrationFormScreen(
              gender = screen.gender,
              onSubmitSuccess = {
                viewModel.navigateTo(KhanScreen.SearchProfiles())
              },
              onNavigateToSearch = {
                viewModel.navigateTo(KhanScreen.SearchProfiles())
              },
              onBack = {
                viewModel.navigateBack()
              },
              onSaveDossier = { newDossier ->
                viewModel.saveDossier(newDossier)
              }
            )
          }
        }

        is KhanScreen.SearchProfiles -> {
          Box(modifier = Modifier.fillMaxSize()) {
            RosePetalsRainOverlay()
            SearchProfilesScreen(
              dossiers = dossiers,
              initialGender = screen.initialGender,
              onNavigateToNewRegistration = {
                viewModel.navigateTo(KhanScreen.GenderSelection())
              },
              onBack = {
                viewModel.navigateBack()
              },
              onDeleteAllDossiers = {
                viewModel.deleteAllDossiers()
              },
              onPopulateSampleProfiles = {
                viewModel.populateSampleProfiles()
              }
            )
          }
        }
      }
    }
  }
}
