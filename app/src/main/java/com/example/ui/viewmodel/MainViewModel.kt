package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiPersonalizationService
import com.example.data.database.AppDatabase
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
import com.example.data.repository.DevotionalRepository
import com.example.ui.util.TtsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object DailyDevotional : Screen()
    object StoriesList : Screen()
    data class StoryDetail(val storyId: String) : Screen()
    object ThirtyDayPlan : Screen()
    object JesusTeachings : Screen()
    object Prayers : Screen()
    object Activities : Screen()
    object Favorites : Screen()
    object Progress : Screen()
    object ParentDashboard : Screen()
    object AiPersonalization : Screen()
    object Settings : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = DevotionalRepository(db.appDao())
    val ttsManager = TtsManager(application)

    // Navigation state with history stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = _screenStack
        .combine(MutableStateFlow(Unit)) { stack, _ -> stack.lastOrNull() ?: Screen.Home }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Screen.Home)

    fun navigateTo(screen: Screen) {
        ttsManager.stop()
        val currentList = _screenStack.value
        if (currentList.lastOrNull() != screen) {
            _screenStack.value = currentList + screen
        }
    }

    fun navigateBack(): Boolean {
        ttsManager.stop()
        val currentList = _screenStack.value
        if (currentList.size > 1) {
            _screenStack.value = currentList.dropLast(1)
            return true
        }
        return false
    }

    // Story Filtering & Search
    private val _selectedCategory = MutableStateFlow(StoryCategory.TODAS)
    val selectedCategory: StateFlow<StoryCategory> = _selectedCategory.asStateFlow()

    private val _selectedAgeBracket = MutableStateFlow("Todas")
    val selectedAgeBracket: StateFlow<String> = _selectedAgeBracket.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setCategory(category: StoryCategory) {
        _selectedCategory.value = category
    }

    fun setAgeBracket(age: String) {
        _selectedAgeBracket.value = age
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Filtered Stories
    val filteredStories: StateFlow<List<BibleStory>> = combine(
        _selectedCategory,
        _selectedAgeBracket,
        _searchQuery
    ) { category, age, query ->
        repository.allStories.filter { story ->
            val matchesCategory = category == StoryCategory.TODAS || story.category == category
            val matchesAge = age == "Todas" || story.ageBracket == "Todas as idades" || story.ageBracket == age
            val matchesQuery = query.isBlank() ||
                    story.title.contains(query, ignoreCase = true) ||
                    story.summary.contains(query, ignoreCase = true) ||
                    story.scriptureReference.contains(query, ignoreCase = true)
            matchesCategory && matchesAge && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.allStories)

    // Database state flows
    val storyProgress: StateFlow<List<StoryProgressEntity>> = repository.storyProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val planProgress: StateFlow<List<PlanProgressEntity>> = repository.planProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val kindnessLogs: StateFlow<List<KindnessLogEntity>> = repository.kindnessLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prayerJournal: StateFlow<List<PrayerJournalEntity>> = repository.prayerRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val childProfiles: StateFlow<List<ChildProfileEntity>> = repository.childProfiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentProfile: StateFlow<ChildProfileEntity?> = repository.currentProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoriteStories: StateFlow<List<BibleStory>> = repository.favoriteStories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Font Size Multiplier for accessibility
    private val _fontSizeScale = MutableStateFlow(1.0f)
    val fontSizeScale: StateFlow<Float> = _fontSizeScale.asStateFlow()

    fun setFontSizeScale(scale: Float) {
        _fontSizeScale.value = scale
    }

    // AI Personalization State
    private val _aiLoading = MutableStateFlow(false)
    val aiLoading: StateFlow<Boolean> = _aiLoading.asStateFlow()

    private val _aiGeneratedResult = MutableStateFlow<String?>(null)
    val aiGeneratedResult: StateFlow<String?> = _aiGeneratedResult.asStateFlow()

    fun generateAiDevotional(childName: String, childAge: Int, topic: String, isPrayer: Boolean) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiGeneratedResult.value = null
            val result = GeminiPersonalizationService.generatePersonalizedDevotional(
                childName = childName.ifBlank { "Amiguinho de Jesus" },
                childAge = childAge,
                topicOrFeeling = topic.ifBlank { "Aprender a amar e ter paz" },
                isPrayer = isPrayer
            )
            _aiGeneratedResult.value = result
            _aiLoading.value = false
        }
    }

    fun clearAiResult() {
        _aiGeneratedResult.value = null
    }

    // Quiz State
    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _selectedQuizOption = MutableStateFlow<Int?>(null)
    val selectedQuizOption: StateFlow<Int?> = _selectedQuizOption.asStateFlow()

    private val _quizAnswerSubmitted = MutableStateFlow(false)
    val quizAnswerSubmitted: StateFlow<Boolean> = _quizAnswerSubmitted.asStateFlow()

    fun selectQuizOption(index: Int) {
        if (!_quizAnswerSubmitted.value) {
            _selectedQuizOption.value = index
        }
    }

    fun submitQuizAnswer() {
        val selected = _selectedQuizOption.value ?: return
        val question = repository.quizQuestions.getOrNull(_currentQuizIndex.value) ?: return
        _quizAnswerSubmitted.value = true
        if (selected == question.correctIndex) {
            _quizScore.value = _quizScore.value + 1
        }
    }

    fun nextQuizQuestion() {
        if (_currentQuizIndex.value + 1 < repository.quizQuestions.size) {
            _currentQuizIndex.value = _currentQuizIndex.value + 1
            _selectedQuizOption.value = null
            _quizAnswerSubmitted.value = false
        }
    }

    fun resetQuiz() {
        _currentQuizIndex.value = 0
        _quizScore.value = 0
        _selectedQuizOption.value = null
        _quizAnswerSubmitted.value = false
    }

    // Actions
    fun toggleFavorite(storyId: String) {
        viewModelScope.launch {
            val isFav = storyProgress.value.find { it.storyId == storyId }?.isFavorite ?: false
            repository.toggleFavorite(storyId, isFav)
        }
    }

    fun markStoryRead(storyId: String) {
        viewModelScope.launch {
            repository.markStoryRead(storyId)
        }
    }

    fun togglePlanDay(dayNumber: Int) {
        viewModelScope.launch {
            val isCompleted = planProgress.value.find { it.dayNumber == dayNumber }?.isCompleted ?: false
            repository.setPlanDayCompleted(dayNumber, !isCompleted)
        }
    }

    fun completeKindnessChallenge(challengeText: String, notes: String = "") {
        viewModelScope.launch {
            val name = currentProfile.value?.name ?: "Pequeno Coração"
            repository.logKindnessChallenge(challengeText, name, notes)
        }
    }

    fun addPrayerRequest(title: String) {
        viewModelScope.launch {
            val name = currentProfile.value?.name ?: "Família"
            repository.addPrayerRequest(title, name)
        }
    }

    fun togglePrayerAnswered(request: PrayerJournalEntity) {
        viewModelScope.launch {
            repository.markPrayerAnswered(request, !request.isAnswered)
        }
    }

    fun deletePrayerRequest(id: Int) {
        viewModelScope.launch {
            repository.deletePrayerRequest(id)
        }
    }

    fun createChildProfile(name: String, age: Int, emoji: String) {
        viewModelScope.launch {
            repository.insertChildProfile(name, age, emoji)
        }
    }

    fun selectProfile(id: Int) {
        viewModelScope.launch {
            repository.selectProfile(id)
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }

    // Narration helper
    fun speakText(text: String) {
        ttsManager.speak(text)
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    init {
        // Initialize default child profile if database has none
        viewModelScope.launch {
            val profiles = childProfiles.first()
            if (profiles.isEmpty()) {
                repository.insertChildProfile("Samuel", 7, "🌟")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
