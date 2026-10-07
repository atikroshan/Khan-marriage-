package com.example.data

import kotlinx.coroutines.flow.Flow

class DossierRepository(private val dao: DossierDao) {
  val allDossiers: Flow<List<CandidateDossier>> = dao.getAllDossiers()

  fun getDossiersByGender(gender: String): Flow<List<CandidateDossier>> {
    return dao.getDossiersByGender(gender)
  }

  suspend fun insertDossier(dossier: CandidateDossier): Long {
    return dao.insertDossier(dossier)
  }

  suspend fun getDossierById(id: Long): CandidateDossier? {
    return dao.getDossierById(id)
  }

  suspend fun deleteDossier(dossier: CandidateDossier) {
    dao.deleteDossier(dossier)
  }

  suspend fun seedIfEmpty() {
    AppDatabase.populateInitialDossiers(dao)
  }
}
