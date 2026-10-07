package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun candidateDossier_creationTest() {
    val dossier = com.example.data.CandidateDossier(
      dossierCode = "KMB-2026-F101",
      candidateName = "Zainab Fatima",
      gender = "Dulhan",
      city = "Lahore",
      age = 24,
      maritalStatus = "Never Married",
      occupation = "Software Engineer",
      education = "Bachelor's / BS (16 Years)"
    )
    assertEquals("KMB-2026-F101", dossier.dossierCode)
    assertEquals("Dulhan", dossier.gender)
    assertEquals(24, dossier.age)
    assertTrue(dossier.isVerified)
  }
}
