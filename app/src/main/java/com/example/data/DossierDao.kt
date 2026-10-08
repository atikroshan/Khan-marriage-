package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DossierDao {
  @Query("SELECT * FROM candidate_dossiers ORDER BY registeredAt DESC")
  fun getAllDossiers(): Flow<List<CandidateDossier>>

  @Query("SELECT * FROM candidate_dossiers WHERE gender = :gender ORDER BY registeredAt DESC")
  fun getDossiersByGender(gender: String): Flow<List<CandidateDossier>>

  @Query("SELECT * FROM candidate_dossiers WHERE id = :id")
  suspend fun getDossierById(id: Long): CandidateDossier?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDossier(dossier: CandidateDossier): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(dossiers: List<CandidateDossier>)

  @Update
  suspend fun updateDossier(dossier: CandidateDossier)

  @Delete
  suspend fun deleteDossier(dossier: CandidateDossier)

  @Query("DELETE FROM candidate_dossiers")
  suspend fun deleteAllDossiers()

  @Query("DELETE FROM candidate_dossiers WHERE dossierCode LIKE 'KMB-%' OR candidateName IN ('Zainab Fatima', 'Hamza Farooq', 'Areeba Maryam', 'Shahmeer Ali Khan')")
  suspend fun deleteDummyDossiers()

  @Query("SELECT COUNT(*) FROM candidate_dossiers")
  suspend fun getCount(): Int
}
