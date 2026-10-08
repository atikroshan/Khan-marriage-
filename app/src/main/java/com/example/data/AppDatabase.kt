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
      // Dummy data removed as requested by user; new real registrations are stored directly
    }
  }
}
