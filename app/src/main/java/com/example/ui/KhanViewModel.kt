package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CandidateDossier
import com.example.data.DossierRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class KhanScreen {
  object Portal : KhanScreen()
  data class GenderSelection(val modeTitle: String? = null) : KhanScreen()
  data class Registration(val gender: String) : KhanScreen()
  data class SearchProfiles(val initialGender: String = "All") : KhanScreen()
}

class KhanViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: DossierRepository

  val dossiers: StateFlow<List<CandidateDossier>>

  var selectedGenderForRegistration: String = "Dulha"

  private val _currentScreen = MutableStateFlow<KhanScreen>(KhanScreen.GenderSelection(null))
  val currentScreen: StateFlow<KhanScreen> = _currentScreen.asStateFlow()

  init {
    val db = AppDatabase.getDatabase(application, viewModelScope)
    repository = DossierRepository(db.dossierDao())
    dossiers = repository.allDossiers.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

    viewModelScope.launch {
      // 1. Delete old mock dummy data
      repository.deleteDummyDossiers()
      // 2. Populate fresh Dulha & Dulhan profiles with images
      repository.seedInitialProfiles(application)
    }
  }

  fun navigateTo(screen: KhanScreen) {
    _currentScreen.value = screen
  }

  fun navigateBack() {
    _currentScreen.value = when (val s = _currentScreen.value) {
      is KhanScreen.Registration -> KhanScreen.Portal
      is KhanScreen.SearchProfiles -> KhanScreen.Portal
      is KhanScreen.GenderSelection -> KhanScreen.GenderSelection(null)
      KhanScreen.Portal -> KhanScreen.GenderSelection(null)
    }
  }

  fun deleteAllDossiers() {
    viewModelScope.launch {
      repository.deleteAllDossiers()
    }
  }

  fun deleteDummyDossiers() {
    viewModelScope.launch {
      repository.deleteDummyDossiers()
    }
  }

  fun populateSampleProfiles() {
    viewModelScope.launch {
      repository.seedInitialProfiles(getApplication())
    }
  }

  suspend fun saveDossier(dossier: CandidateDossier): Long {
    return repository.insertDossier(dossier)
  }
}
