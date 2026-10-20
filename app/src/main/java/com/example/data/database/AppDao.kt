package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Stories
    @Query("SELECT * FROM story_progress")
    fun getAllStoryProgress(): Flow<List<StoryProgressEntity>>

    @Query("SELECT * FROM story_progress WHERE storyId = :storyId LIMIT 1")
    suspend fun getStoryProgressById(storyId: String): StoryProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStoryProgress(progress: StoryProgressEntity)

    @Query("UPDATE story_progress SET isFavorite = :isFavorite WHERE storyId = :storyId")
    suspend fun updateFavorite(storyId: String, isFavorite: Boolean)

    @Query("SELECT * FROM story_progress WHERE isFavorite = 1")
    fun getFavoriteStories(): Flow<List<StoryProgressEntity>>

    // 30-day Plan
    @Query("SELECT * FROM plan_progress")
    fun getAllPlanProgress(): Flow<List<PlanProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlanDayCompleted(progress: PlanProgressEntity)

    // Kindness logs
    @Query("SELECT * FROM kindness_logs ORDER BY completedTimestamp DESC")
    fun getAllKindnessLogs(): Flow<List<KindnessLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKindnessLog(log: KindnessLogEntity)

    // Prayer journal
    @Query("SELECT * FROM prayer_journal ORDER BY createdTimestamp DESC")
    fun getAllPrayerRequests(): Flow<List<PrayerJournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerRequest(request: PrayerJournalEntity)

    @Update
    suspend fun updatePrayerRequest(request: PrayerJournalEntity)

    @Query("DELETE FROM prayer_journal WHERE id = :id")
    suspend fun deletePrayerRequest(id: Int)

    // Child Profiles
    @Query("SELECT * FROM child_profiles")
    fun getAllProfiles(): Flow<List<ChildProfileEntity>>

    @Query("SELECT * FROM child_profiles WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentProfile(): Flow<ChildProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ChildProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: ChildProfileEntity)

    @Query("UPDATE child_profiles SET isCurrent = 0")
    suspend fun clearCurrentProfiles()

    @Query("UPDATE child_profiles SET isCurrent = 1 WHERE id = :id")
    suspend fun selectCurrentProfile(id: Int)

    @Query("DELETE FROM story_progress")
    suspend fun resetStoryProgress()

    @Query("DELETE FROM plan_progress")
    suspend fun resetPlanProgress()

    @Query("DELETE FROM kindness_logs")
    suspend fun resetKindnessLogs()
}
