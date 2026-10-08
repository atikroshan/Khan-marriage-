package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

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

  suspend fun deleteAllDossiers() {
    dao.deleteAllDossiers()
  }

  suspend fun deleteDummyDossiers() {
    dao.deleteDummyDossiers()
  }

  suspend fun seedInitialProfiles(context: Context) {
    if (dao.getCount() > 0) return

    val photoDir = File(context.filesDir, "candidate_photos").apply { mkdirs() }
    val photo1Dulha = File(photoDir, "kmb001_1.jpg")
    val photo2Dulha = File(photoDir, "kmb001_2.jpg")
    val photo1Dulhan = File(photoDir, "kmb002_1.jpg")
    val photo2Dulhan = File(photoDir, "kmb002_2.jpg")

    val dulhaBytes = GoogleSheetsDriveService.readImageBytes(
      context = context,
      imageUriString = AppDatabase.DULHA_IMAGE_URL,
      serialCode = "kmb001",
      photoIndex = 1,
      gender = "Dulha"
    )
    photo1Dulha.writeBytes(dulhaBytes)
    photo2Dulha.writeBytes(dulhaBytes)

    val dulhanBytes = GoogleSheetsDriveService.readImageBytes(
      context = context,
      imageUriString = AppDatabase.DULHAN_IMAGE_URL,
      serialCode = "kmb002",
      photoIndex = 1,
      gender = "Dulhan"
    )
    photo1Dulhan.writeBytes(dulhanBytes)
    photo2Dulhan.writeBytes(dulhanBytes)

    val dulhaDossier = CandidateDossier(
      dossierCode = "kmb001",
      candidateName = "Rehan Ahmed Khan",
      gender = "Dulha",
      city = "Solapur",
      age = 27,
      maritalStatus = "Never Married",
      occupation = "Software Developer / Programmer",
      education = "MCA / M.Sc. IT",
      complexion = "Fair",
      height = "5'9\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Respected Khan family settled in Solapur. Father retired Govt officer, 1 sister married.",
      contactNumber = "+91 98123 45678",
      frontPortraitUrl = Uri.fromFile(photo1Dulha).toString(),
      fullLengthUrl = Uri.fromFile(photo2Dulha).toString(),
      isVerified = true
    )

    val dulhanDossier = CandidateDossier(
      dossierCode = "kmb002",
      candidateName = "Zoya Fatima Khan",
      gender = "Dulhan",
      city = "Mumbai",
      age = 24,
      maritalStatus = "Never Married",
      occupation = "Teacher / Professor / Lecturer",
      education = "M.Sc. (Master of Science)",
      complexion = "Fair",
      height = "5'4\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Educated Sunni Muslim family settled in Mumbai. Father businessman, mother homemaker.",
      contactNumber = "+91 98765 43210",
      frontPortraitUrl = Uri.fromFile(photo1Dulhan).toString(),
      fullLengthUrl = Uri.fromFile(photo2Dulhan).toString(),
      isVerified = true
    )

    dao.insertAll(listOf(dulhaDossier, dulhanDossier))
  }
}
