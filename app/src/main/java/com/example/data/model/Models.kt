package com.example.data.model

enum class StoryCategory(val displayName: String, val emoji: String) {
    TODAS("Todas", "✨"),
    CRIACAO("Criação & Começo", "🌍"),
    HEROIS_FE("Heróis da Fé", "🛡️"),
    VIDA_JESUS("Vida de Jesus", "⭐"),
    PARABOLAS("Parábolas", "🌱"),
    MILAGRES("Milagres", "✨"),
    AMOR_BONDADE("Amor & Bondade", "❤️"),
    PRIMEIROS_CRISTAOS("Primeiros Cristãos", "🕊️")
}

data class BibleStory(
    val id: String,
    val title: String,
    val testament: String, // "Antigo Testamento" ou "Novo Testamento"
    val category: StoryCategory,
    val scriptureReference: String,
    val ageBracket: String, // "3-5 anos", "6-8 anos", "9-12 anos"
    val readingMinutes: Int,
    val summary: String,
    val storyParagraphs: List<String>,
    val memoryVerse: String,
    val memoryVerseRef: String,
    val whatWeLearn: String,
    val familyDiscussion: List<String>,
    val childPrayer: String,
    val kindnessAction: String,
    val iconEmoji: String
)

data class DevotionalDay(
    val dayNumber: Int,
    val title: String,
    val theme: String,
    val scriptureRef: String,
    val verseText: String,
    val message: String,
    val practicalTip: String,
    val prayer: String,
    val kindnessChallenge: String
)

data class PrayerItem(
    val id: String,
    val category: String, // "Manhã", "Dormir", "Gratidão", "Medo", "Família", "Refeição", "Perdão", "Moçambique & Mundo"
    val title: String,
    val prayerText: String,
    val verseReference: String,
    val emoji: String
)

data class JesusTeaching(
    val id: String,
    val title: String,
    val subtitle: String,
    val scriptureRef: String,
    val emoji: String,
    val teachingSummary: String,
    val practicalExample: String,
    val questionForKids: String,
    val actionChallenge: String
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class BadgeDef(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val requiredCount: Int,
    val type: String // "stories", "devotionals", "kindness", "prayers"
)
