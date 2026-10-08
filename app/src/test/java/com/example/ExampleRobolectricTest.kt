package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CandidateDossier
import com.example.data.GoogleSheetsDriveService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ExampleRobolectricTest {

  private lateinit var db: com.example.data.AppDatabase

  @Before
  fun setUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = androidx.room.Room.inMemoryDatabaseBuilder(
      context,
      com.example.data.AppDatabase::class.java
    ).allowMainThreadQueries().build()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Khan Marriage Bureau", appName)
  }

  @Test
  fun `verify Google Sheet row columns structure matches Groom tab columns`() {
    val demoGroomDossier = CandidateDossier(
      id = 1,
      dossierCode = "kmb001",
      candidateName = "Rehan Ahmed Khan",
      gender = "Dulha",
      city = "Solapur",
      age = 27,
      maritalStatus = "Never Married",
      occupation = "Software Developer",
      education = "MCA",
      complexion = "Fair",
      height = "5'9\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Respectable family in Solapur",
      contactNumber = "+91 98123 45678",
      frontPortraitUrl = "https://example.com/photo1.jpg",
      fullLengthUrl = "https://example.com/photo2.jpg",
      isVerified = true
    )

    val folderShareUrl = "https://drive.google.com/drive/folders/folder_kmb001?usp=sharing"
    val rowValues = GoogleSheetsDriveService.buildSheetRowValues(
      dossier = demoGroomDossier,
      serialCode = "kmb001",
      photoFolderUrl = folderShareUrl,
      targetTabName = "Groom"
    )

    // Groom tab: 9 columns (Serial no., City, Age, Martial Status, Occupation, Education, Color, Height, Photo url)
    assertEquals(9, rowValues.length())
    assertEquals("kmb001", rowValues.getString(0)) // Column A: Serial no.
    assertEquals("Solapur", rowValues.getString(1)) // Column B: City
    assertEquals("27", rowValues.getString(2))      // Column C: Age
    assertEquals("Never Married", rowValues.getString(3)) // Column D: Martial Status
    assertEquals("Software Developer", rowValues.getString(4)) // Column E: Occupation
    assertEquals("MCA", rowValues.getString(5))     // Column F: Education
    assertEquals("Fair", rowValues.getString(6))    // Column G: Color
    assertEquals("5'9\"", rowValues.getString(7))   // Column H: Height
    assertEquals(folderShareUrl, rowValues.getString(8)) // Column I: Photo url
  }

  @Test
  fun `verify Google Sheet row columns structure matches Bride tab columns`() {
    val demoBrideDossier = CandidateDossier(
      id = 2,
      dossierCode = "kmb002",
      candidateName = "Zoya Fatima Khan",
      gender = "Dulhan",
      city = "Mumbai",
      age = 24,
      maritalStatus = "Never Married",
      occupation = "Teacher",
      education = "M.Sc.",
      complexion = "Fair",
      height = "5'4\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Educated family in Mumbai",
      contactNumber = "+91 98765 43210",
      frontPortraitUrl = "https://example.com/bride1.jpg",
      fullLengthUrl = "https://example.com/bride2.jpg",
      isVerified = true
    )

    val folderShareUrl = "https://drive.google.com/drive/folders/folder_kmb002?usp=sharing"
    val rowValues = GoogleSheetsDriveService.buildSheetRowValues(
      dossier = demoBrideDossier,
      serialCode = "kmb002",
      photoFolderUrl = folderShareUrl,
      targetTabName = "Bride"
    )

    // Bride tab: 10 columns (Reg. Mobile, Serial no., City, Age, Martial Status, Occupation, Education, Color, Height, Photo url)
    assertEquals(10, rowValues.length())
    assertEquals("+91 98765 43210", rowValues.getString(0)) // Column A: Reg. Mobile
    assertEquals("kmb002", rowValues.getString(1)) // Column B: Serial no.
    assertEquals("Mumbai", rowValues.getString(2)) // Column C: City
    assertEquals("24", rowValues.getString(3))     // Column D: Age
    assertEquals("Never Married", rowValues.getString(4)) // Column E: Martial Status
    assertEquals("Teacher", rowValues.getString(5)) // Column F: Occupation
    assertEquals("M.Sc.", rowValues.getString(6))  // Column G: Education
    assertEquals("Fair", rowValues.getString(7))   // Column H: Color
    assertEquals("5'4\"", rowValues.getString(8))  // Column I: Height
    assertEquals(folderShareUrl, rowValues.getString(9)) // Column J: Photo url
  }

  @Test
  fun `verify delete all dummy data clears database cleanly`() = runBlocking {
    val dao = db.dossierDao()

    val dummy1 = CandidateDossier(
      dossierCode = "KMB-2026-F101",
      candidateName = "Zainab Fatima",
      gender = "Dulhan",
      city = "Solapur",
      age = 24,
      maritalStatus = "Never Married",
      occupation = "Engineer",
      education = "B.Tech",
      complexion = "Fair",
      height = "5'4\"",
      casteSect = "Sunni",
      familyDetails = "Family",
      contactNumber = "123",
      frontPortraitUrl = "",
      fullLengthUrl = "",
      isVerified = true
    )
    dao.insertDossier(dummy1)
    assertEquals(1, dao.getCount())

    // Delete all dummy data
    dao.deleteAllDossiers()
    assertEquals(0, dao.getCount())
  }

  @Test
  fun `verify new registration for both Dulha and Dulhan with photos shows up in search`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val dao = db.dossierDao()

    // Prepare local photo files
    val photoDir = File(context.filesDir, "test_photos").apply { mkdirs() }
    val groomPhoto = File(photoDir, "kmb001_1.jpg").apply {
      writeBytes(GoogleSheetsDriveService.generateDummyPhotoBytes("kmb001", 1, "Dulha"))
    }
    val bridePhoto = File(photoDir, "kmb002_1.jpg").apply {
      writeBytes(GoogleSheetsDriveService.generateDummyPhotoBytes("kmb002", 1, "Dulhan"))
    }

    // 1. Add New Registration for Dulha (Groom) with photo
    val newDulha = CandidateDossier(
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
      familyDetails = "Respected Khan family settled in Solapur.",
      contactNumber = "+91 98123 45678",
      frontPortraitUrl = groomPhoto.toURI().toString(),
      fullLengthUrl = groomPhoto.toURI().toString(),
      isVerified = true
    )
    dao.insertDossier(newDulha)

    // 2. Add New Registration for Dulhan (Bride) with photo
    val newDulhan = CandidateDossier(
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
      familyDetails = "Educated Sunni Muslim family settled in Mumbai.",
      contactNumber = "+91 98765 43210",
      frontPortraitUrl = bridePhoto.toURI().toString(),
      fullLengthUrl = bridePhoto.toURI().toString(),
      isVerified = true
    )
    dao.insertDossier(newDulhan)

    // Verify search profiles queries
    val allProfiles = dao.getAllDossiers().first()
    assertEquals(2, allProfiles.size)

    val dulhaProfiles = dao.getDossiersByGender("Dulha").first()
    assertEquals(1, dulhaProfiles.size)
    assertEquals("Rehan Ahmed Khan", dulhaProfiles[0].candidateName)
    assertTrue(dulhaProfiles[0].frontPortraitUrl.contains("kmb001_1.jpg"))

    val dulhanProfiles = dao.getDossiersByGender("Dulhan").first()
    assertEquals(1, dulhanProfiles.size)
    assertEquals("Zoya Fatima Khan", dulhanProfiles[0].candidateName)
    assertTrue(dulhanProfiles[0].frontPortraitUrl.contains("kmb002_1.jpg"))
  }

  @Test
  fun `verify seedInitialProfiles seeds both Dulha and Dulhan with valid photos`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = com.example.data.DossierRepository(db.dossierDao())

    repository.seedInitialProfiles(context)

    val count = db.dossierDao().getCount()
    assertEquals(2, count)

    val all = db.dossierDao().getAllDossiers().first()
    val dulha = all.find { it.gender.equals("Dulha", true) }
    val dulhan = all.find { it.gender.equals("Dulhan", true) }

    assertNotNull(dulha)
    assertNotNull(dulhan)
    assertEquals("kmb001", dulha!!.dossierCode)
    assertEquals("kmb002", dulhan!!.dossierCode)
    assertTrue(dulha.frontPortraitUrl.isNotBlank())
    assertTrue(dulhan.frontPortraitUrl.isNotBlank())
  }

  @Test
  fun `verify Groom demo data sync and folder creation with photo renaming`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()

    val demoGroomDossier = CandidateDossier(
      id = 1,
      dossierCode = "kmb001",
      candidateName = "Zaid Khan",
      gender = "Dulha",
      city = "Mumbai",
      age = 27,
      maritalStatus = "Never Married",
      occupation = "Software Professional / IT Engineer",
      education = "Engineer (B.E., B.Tech)",
      complexion = "Very Fair",
      height = "5'10\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Well-established family in Bandra, Mumbai",
      contactNumber = "+91 98200 54321",
      frontPortraitUrl = "https://example.com/groom1.jpg",
      fullLengthUrl = "https://example.com/groom2.jpg",
      isVerified = true
    )

    val syncResult = GoogleSheetsDriveService.syncRegistration(
      context = context,
      dossier = demoGroomDossier,
      sequenceCount = 1,
      image1Uri = "https://example.com/groom1.jpg",
      image2Uri = "https://example.com/groom2.jpg"
    )

    assertEquals("kmb001", syncResult.serialCode)
    assertEquals(GoogleSheetsDriveService.FOLDER_NAME_DULHA, syncResult.genderFolder)
    assertEquals(GoogleSheetsDriveService.TAB_NAME_GROOM, syncResult.sheetTabName)
    assertTrue(syncResult.candidateFolderUrl.contains("kmb001"))
    assertNotNull(syncResult.photo1DriveUrl)
    assertNotNull(syncResult.photo2DriveUrl)
    assertTrue(syncResult.photo1DriveUrl!!.contains("kmb001_1.jpg"))
    assertTrue(syncResult.photo2DriveUrl!!.contains("kmb001_2.jpg"))
  }

  @Test
  fun `verify Bride demo data sync and folder creation with photo renaming`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()

    val demoBrideDossier = CandidateDossier(
      id = 2,
      dossierCode = "kmb002",
      candidateName = "Ayesha Patel",
      gender = "Dulhan",
      city = "Pune",
      age = 23,
      maritalStatus = "Never Married",
      occupation = "Architect / Designer",
      education = "B.Arch",
      complexion = "Fair",
      height = "5'5\"",
      casteSect = "Sunni / Muslim",
      familyDetails = "Reputed family residing in Pune",
      contactNumber = "+91 98900 67890",
      frontPortraitUrl = "https://example.com/bride1.jpg",
      fullLengthUrl = "https://example.com/bride2.jpg",
      isVerified = true
    )

    val syncResult = GoogleSheetsDriveService.syncRegistration(
      context = context,
      dossier = demoBrideBrideDossier(demoBrideDossier),
      sequenceCount = 2,
      image1Uri = "https://example.com/bride1.jpg",
      image2Uri = "https://example.com/bride2.jpg"
    )

    assertEquals("kmb002", syncResult.serialCode)
    assertEquals(GoogleSheetsDriveService.FOLDER_NAME_DULHAN, syncResult.genderFolder)
    assertEquals(GoogleSheetsDriveService.TAB_NAME_BRIDE, syncResult.sheetTabName)
    assertTrue(syncResult.candidateFolderUrl.contains("kmb002"))
    assertNotNull(syncResult.photo1DriveUrl)
    assertNotNull(syncResult.photo2DriveUrl)
    assertTrue(syncResult.photo1DriveUrl!!.contains("kmb002_1.jpg"))
    assertTrue(syncResult.photo2DriveUrl!!.contains("kmb002_2.jpg"))
  }

  private fun demoBrideBrideDossier(dossier: CandidateDossier) = dossier

  @Test
  fun `verify dummy photo generation produces valid JPEG bytes without crash`() {
    val bytes1 = GoogleSheetsDriveService.generateDummyPhotoBytes("kmb001", 1, "Dulhan")
    val bytes2 = GoogleSheetsDriveService.generateDummyPhotoBytes("kmb001", 2, "Dulha")

    assertTrue(bytes1.isNotEmpty())
    assertTrue(bytes2.isNotEmpty())

    // Standard JPEG start-of-image SOI marker: 0xFF, 0xD8
    assertEquals(0xFF.toByte(), bytes1[0])
    assertEquals(0xD8.toByte(), bytes1[1])
    assertEquals(0xFF.toByte(), bytes2[0])
    assertEquals(0xD8.toByte(), bytes2[1])
  }

  @Test
  fun `verify readImageBytes never throws FileNotFoundException on https URLs`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val testHttpsUrl = "https://lh3.googleusercontent.com/aida-public/test-image.jpg"

    val bytes = GoogleSheetsDriveService.readImageBytes(
      context = context,
      imageUriString = testHttpsUrl,
      serialCode = "kmb001",
      photoIndex = 1,
      gender = "Dulhan"
    )

    assertTrue(bytes.isNotEmpty())
    assertEquals(0xFF.toByte(), bytes[0])
    assertEquals(0xD8.toByte(), bytes[1])
  }
}
