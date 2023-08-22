package com.faithckorir.mytraveltales.apputils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

@Database(entities = [DiaryEntry::class], version = 1)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
@Dao
interface DiaryDao {
    @Query("SELECT * FROM DiaryEntry")
    suspend fun getAllEntries(): List<DiaryEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: DiaryEntry)
}
class DiaryRepository(private val diaryDao: DiaryDao) {
    suspend fun getAllEntries(): List<DiaryEntry> = diaryDao.getAllEntries()

    suspend fun insertEntry(entry: DiaryEntry) = diaryDao.insertEntry(entry)
}
class DiaryViewModel(private val repository: DiaryRepository) : ViewModel() {
    val allEntries: LiveData<List<DiaryEntry>> = liveData {
        val entries = repository.getAllEntries()
        emit(entries)
    }

    fun insertEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            repository.insertEntry(entry)
        }
    }
}


@Entity
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: String,
    val location: String,
    val notes: String,
    val photos: List<String> // URLs or local paths to photos
)

