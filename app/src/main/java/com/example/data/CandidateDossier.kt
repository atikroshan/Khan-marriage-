package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate_dossiers")
data class CandidateDossier(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val dossierCode: String,
  val candidateName: String,
  val gender: String, // "Dulhan" or "Dulha"
  val city: String,
  val age: Int,
  val maritalStatus: String,
  val occupation: String,
  val education: String,
  val complexion: String = "Fair",
  val height: String = "5'5\"",
  val casteSect: String = "Sunni / Khan Pathan",
  val familyDetails: String = "Respected, educated family",
  val contactNumber: String = "+92 300 1234567",
  val frontPortraitUrl: String = "",
  val fullLengthUrl: String = "",
  val isVerified: Boolean = true,
  val registeredAt: Long = System.currentTimeMillis()
)
