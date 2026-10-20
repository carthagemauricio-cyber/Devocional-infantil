package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_progress")
data class StoryProgressEntity(
    @PrimaryKey val storyId: String,
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val readCount: Int = 0,
    val lastReadTimestamp: Long = 0L
)

@Entity(tableName = "plan_progress")
data class PlanProgressEntity(
    @PrimaryKey val dayNumber: Int,
    val isCompleted: Boolean = false,
    val completedTimestamp: Long = 0L
)

@Entity(tableName = "kindness_logs")
data class KindnessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val challengeText: String,
    val completedTimestamp: Long = System.currentTimeMillis(),
    val childName: String = "",
    val userNotes: String = ""
)

@Entity(tableName = "prayer_journal")
data class PrayerJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val childName: String = "",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val isAnswered: Boolean = false,
    val answeredNotes: String = ""
)

@Entity(tableName = "child_profiles")
data class ChildProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int = 6,
    val avatarEmoji: String = "🌟",
    val streakDays: Int = 1,
    val lastActiveDate: String = "",
    val isCurrent: Boolean = false
)
