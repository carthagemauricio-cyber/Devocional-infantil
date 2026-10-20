package com.example.data.repository

import com.example.data.content.ActivitiesContent
import com.example.data.content.PrayersAndTeachingsContent
import com.example.data.content.StoriesEasterAndEarlyChurch
import com.example.data.content.StoriesJesusLife
import com.example.data.content.StoriesOldTestament
import com.example.data.content.StoriesParablesAndMiracles
import com.example.data.content.ThirtyDayPlanContent
import com.example.data.database.AppDao
import com.example.data.database.ChildProfileEntity
import com.example.data.database.KindnessLogEntity
import com.example.data.database.PlanProgressEntity
import com.example.data.database.PrayerJournalEntity
import com.example.data.database.StoryProgressEntity
import com.example.data.model.BadgeDef
import com.example.data.model.BibleStory
import com.example.data.model.DevotionalDay
import com.example.data.model.JesusTeaching
import com.example.data.model.PrayerItem
import com.example.data.model.QuizQuestion
import com.example.data.model.StoryCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class DevotionalRepository(private val appDao: AppDao) {

    val allStories: List<BibleStory> by lazy {
        StoriesOldTestament.stories +
                StoriesJesusLife.stories +
                StoriesParablesAndMiracles.stories +
                StoriesEasterAndEarlyChurch.stories
    }

    val thirtyDayPlan: List<DevotionalDay> = ThirtyDayPlanContent.planDays
    val prayers: List<PrayerItem> = PrayersAndTeachingsContent.prayers
    val jesusTeachings: List<JesusTeaching> = PrayersAndTeachingsContent.jesusTeachings
    val quizQuestions: List<QuizQuestion> = ActivitiesContent.quizQuestions
    val badgesDef: List<BadgeDef> = ActivitiesContent.badges
    val kindnessChallengesList: List<String> = ActivitiesContent.kindnessChallengesList

    fun getStoryById(id: String): BibleStory? {
        return allStories.find { it.id == id }
    }

    fun getPlanDay(day: Int): DevotionalDay {
        return thirtyDayPlan.find { it.dayNumber == day } ?: thirtyDayPlan.first()
    }

    fun getTodayDevotionalDay(): DevotionalDay {
        val cal = Calendar.getInstance()
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val dayIndex = ((dayOfYear - 1) % 30) + 1
        return getPlanDay(dayIndex)
    }

    // Room Database Observables
    val storyProgress: Flow<List<StoryProgressEntity>> = appDao.getAllStoryProgress()
    val planProgress: Flow<List<PlanProgressEntity>> = appDao.getAllPlanProgress()
    val kindnessLogs: Flow<List<KindnessLogEntity>> = appDao.getAllKindnessLogs()
    val prayerRequests: Flow<List<PrayerJournalEntity>> = appDao.getAllPrayerRequests()
    val childProfiles: Flow<List<ChildProfileEntity>> = appDao.getAllProfiles()
    val currentProfile: Flow<ChildProfileEntity?> = appDao.getCurrentProfile()

    // Combined Flow for Favorite Stories
    val favoriteStories: Flow<List<BibleStory>> = appDao.getAllStoryProgress().map { progressList ->
        val favoriteIds = progressList.filter { it.isFavorite }.map { it.storyId }.toSet()
        allStories.filter { it.id in favoriteIds }
    }

    // Actions
    suspend fun toggleFavorite(storyId: String, currentFavorite: Boolean) {
        val existing = appDao.getStoryProgressById(storyId)
        if (existing != null) {
            appDao.updateFavorite(storyId, !currentFavorite)
        } else {
            appDao.insertOrUpdateStoryProgress(
                StoryProgressEntity(
                    storyId = storyId,
                    isFavorite = !currentFavorite,
                    isRead = false
                )
            )
        }
    }

    suspend fun markStoryRead(storyId: String) {
        val existing = appDao.getStoryProgressById(storyId)
        if (existing != null) {
            appDao.insertOrUpdateStoryProgress(
                existing.copy(
                    isRead = true,
                    readCount = existing.readCount + 1,
                    lastReadTimestamp = System.currentTimeMillis()
                )
            )
        } else {
            appDao.insertOrUpdateStoryProgress(
                StoryProgressEntity(
                    storyId = storyId,
                    isRead = true,
                    readCount = 1,
                    lastReadTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun setPlanDayCompleted(day: Int, completed: Boolean) {
        appDao.setPlanDayCompleted(
            PlanProgressEntity(
                dayNumber = day,
                isCompleted = completed,
                completedTimestamp = if (completed) System.currentTimeMillis() else 0L
            )
        )
    }

    suspend fun logKindnessChallenge(challengeText: String, childName: String, notes: String = "") {
        appDao.insertKindnessLog(
            KindnessLogEntity(
                challengeText = challengeText,
                childName = childName,
                userNotes = notes,
                completedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun addPrayerRequest(title: String, childName: String) {
        appDao.insertPrayerRequest(
            PrayerJournalEntity(
                title = title,
                childName = childName,
                createdTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun markPrayerAnswered(request: PrayerJournalEntity, isAnswered: Boolean) {
        appDao.updatePrayerRequest(request.copy(isAnswered = isAnswered))
    }

    suspend fun deletePrayerRequest(id: Int) {
        appDao.deletePrayerRequest(id)
    }

    suspend fun ensureDefaultProfile() {
        // Will check in ViewModel and insert initial child profile if empty
    }

    suspend fun insertChildProfile(name: String, age: Int, avatarEmoji: String) {
        appDao.clearCurrentProfiles()
        appDao.insertProfile(
            ChildProfileEntity(
                name = name,
                age = age,
                avatarEmoji = avatarEmoji,
                isCurrent = true
            )
        )
    }

    suspend fun selectProfile(id: Int) {
        appDao.clearCurrentProfiles()
        appDao.selectCurrentProfile(id)
    }

    suspend fun resetAllProgress() {
        appDao.resetStoryProgress()
        appDao.resetPlanProgress()
        appDao.resetKindnessLogs()
    }
}
