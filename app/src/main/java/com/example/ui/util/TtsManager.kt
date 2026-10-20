package com.example.ui.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val mozambiqueLocale = Locale.forLanguageTag("pt-MZ")
            val portugueseLocale = Locale.forLanguageTag("pt-PT")
            val defaultPt = Locale.forLanguageTag("pt")

            val result = tts?.setLanguage(mozambiqueLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                val resPt = tts?.setLanguage(portugueseLocale)
                if (resPt == TextToSpeech.LANG_MISSING_DATA || resPt == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(defaultPt)
                }
            }

            tts?.setSpeechRate(0.92f) // Gentle, calm child-friendly pace
            tts?.setPitch(1.05f)     // Warm, pleasant tone

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
            isInitialized = true
        }
    }

    fun speak(text: String) {
        if (!isInitialized) return
        stop()
        _currentText.value = text
        _isSpeaking.value = true
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "devotional_narration")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentText.value = ""
    }

    fun setSpeed(speed: Float) {
        tts?.setSpeechRate(speed)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
