package com.example.data.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val imageBase64: String? = null,
    val language: String = "English",
    val isError: Boolean = false
)

data class StudyNote(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val subject: String,
    val content: String,
    val tags: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class McqQuestion(
    val id: String = UUID.randomUUID().toString(),
    val question: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val subject: String = "General Science",
    val difficulty: String = "Medium"
)

data class MathSolution(
    val problem: String,
    val category: String,
    val steps: List<String>,
    val finalAnswer: String,
    val formulaOrMethod: String
)

data class ScienceConcept(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: String, // "Physics", "Chemistry", "Biology", "General Science"
    val simpleExplanation: String,
    val detailedExplanation: String,
    val examples: List<String>,
    val keyTakeaways: List<String>
)

data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val subject: String,
    val front: String,
    val back: String,
    val isMastered: Boolean = false
)

data class DictionaryEntry(
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val example: String,
    val synonyms: List<String>
)

data class UserProfile(
    val name: String = "Miraj Alam",
    val email: String = "mirajalam98917@gmail.com",
    val isPro: Boolean = true,
    val streakDays: Int = 14,
    val totalStudyHours: Float = 28.5f,
    val questionsSolved: Int = 168,
    val notesCreated: Int = 24
)
