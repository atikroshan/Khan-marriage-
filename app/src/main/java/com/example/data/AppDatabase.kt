package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CandidateDossier::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun dossierDao(): DossierDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    const val DULHA_IMAGE_URL =
      "https://lh3.googleusercontent.com/aida-public/AB6AXuDXHYt5BR3jZji6eAcslZUEZkrbiHK_IwhADr8shwI_16-oGcE3FXdeE0kd964ySUg0rzOF_s-p43qhJOwk0fJbMcvvTA8B8Zz1dWBB1avwNaC8YlkXqV3ztu8diigj1-hZe37m8ZN_N2ECKbL6Q3bRoLFWhP9TUBMz0cJ3bR3U-uv7QjSMHORTH1Ti7o80gT-gUJg5h4YX73xOZLWty_NyhHq4_77WFGaQ4ib0n-lxBgW8ebG7qSz1lIBO1kHHLVlxmQ"

    const val DULHAN_IMAGE_URL =
      "https://lh3.googleusercontent.com/aida-public/AB6AXuBoqVXfGdpAEzZHZTzKjaSLSchoz52B2PhWgKW8-qK3D8-U0x7dXS5YxqwJJRRk1e6z-fhnJXOjmYc-c8-7K6cnwAGzcbSITt9RSW5KiOW4pSTMHLgd0hthgKLCk6CxFCXeRH8pfZwHc2dD_x_VVXzx5XE-DJvK19wMnow2lDw-ZoVr1H3DBj5RcYwKj3I7Nvm38O1J5YIhmnBbX-1ESy3AfZ22tnJj_0PoeLkXspvDjKB5qQk0naDx-7Ky1koVTVI9Zg"

    const val HANDS_BANNER_URL =
      "https://lh3.googleusercontent.com/aida-public/AB6AXuAs9IGgk4lJN7MMg40qfMpC42LXjv5jwFu0BeE2z8rWFSvgfSIClCkCi8i6062j2zbWSNMuz8Q3-AAvWM6zrsKuuGIqyyekHZI4wx3YJs4y_KuQcVpnPjtVAtp3G3juQSF0sm7N5zKGw9IpLOmum8Ajoi2CbunyfI_soJtY189WuDxy6AeG-Jr7B6O0c8EzGX4Y-mBCD6IE4ZoOIaSWGU0Q08kh92GKM8ClGUOs2p9dUNpXrDMGeLzYEF7LyxwKNE5MnA"

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "khan_marriage_bureau_db"
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabaseCallback(scope))
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateInitialDossiers(database.dossierDao())
          }
        }
      }
    }

    suspend fun populateInitialDossiers(dao: DossierDao) {
      if (dao.getCount() > 0) return

      val initialList = listOf(
        CandidateDossier(
          dossierCode = "KMB-2026-F101",
          candidateName = "Zainab Fatima",
          gender = "Dulhan",
          city = "Solapur",
          age = 24,
          maritalStatus = "Never Married",
          occupation = "Software Engineer",
          education = "Bachelor's / BS (16 Years)",
          complexion = "Fair",
          height = "5'4\"",
          casteSect = "Sunni / Khan Pathan",
          familyDetails = "Father is a retired Civil Engineer, Mother is an educator. 2 siblings.",
          contactNumber = "+91 98231 45890",
          frontPortraitUrl = DULHAN_IMAGE_URL,
          fullLengthUrl = DULHAN_IMAGE_URL,
          isVerified = true
        ),
        CandidateDossier(
          dossierCode = "KMB-2026-M201",
          candidateName = "Hamza Farooq",
          gender = "Dulha",
          city = "Mumbai",
          age = 28,
          maritalStatus = "Never Married",
          occupation = "Doctor / Physician",
          education = "Medical Degree (MBBS/BDS)",
          complexion = "Very Fair",
          height = "5'11\"",
          casteSect = "Sunni / Khan Niazi",
          familyDetails = "Practicing physician at Multi-speciality Hospital. Well-established family in Bandra.",
          contactNumber = "+91 98220 78192",
          frontPortraitUrl = DULHA_IMAGE_URL,
          fullLengthUrl = DULHA_IMAGE_URL,
          isVerified = true
        ),
        CandidateDossier(
          dossierCode = "KMB-2026-F102",
          candidateName = "Areeba Maryam",
          gender = "Dulhan",
          city = "Pune",
          age = 26,
          maritalStatus = "Never Married",
          occupation = "Chartered Accountant",
          education = "ACCA / CA",
          complexion = "Fair",
          height = "5'5\"",
          casteSect = "Sunni / Siddiqui",
          familyDetails = "Working with MNC audit firm. Religious, dignified family in Kothrud.",
          contactNumber = "+91 94230 11984",
          frontPortraitUrl = DULHAN_IMAGE_URL,
          fullLengthUrl = DULHAN_IMAGE_URL,
          isVerified = true
        ),
        CandidateDossier(
          dossierCode = "KMB-2026-M202",
          candidateName = "Shahmeer Ali Khan",
          gender = "Dulha",
          city = "Hyderabad",
          age = 29,
          maritalStatus = "Never Married",
          occupation = "Business Owner / Entrepreneur",
          education = "Master's Degree (MS/MPhil)",
          complexion = "Wheatish / Medium",
          height = "6'0\"",
          casteSect = "Sunni / Durrani",
          familyDetails = "Owner of manufacturing business unit. Prominent family in Banjara Hills.",
          contactNumber = "+91 98490 33412",
          frontPortraitUrl = DULHA_IMAGE_URL,
          fullLengthUrl = DULHA_IMAGE_URL,
          isVerified = true
        )
      )
      dao.insertAll(initialList)
    }
  }
}
