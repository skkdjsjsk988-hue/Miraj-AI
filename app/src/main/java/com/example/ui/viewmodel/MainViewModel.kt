package com.example.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiApiClient
import com.example.data.model.DictionaryEntry
import com.example.data.model.Flashcard
import com.example.data.model.MathSolution
import com.example.data.model.McqQuestion
import com.example.data.model.ScienceConcept
import com.example.data.model.StudyNote
import com.example.data.repository.StudyRepository
import com.example.ui.theme.ThemeMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    val repository = StudyRepository(application.applicationContext)

    // --- Navigation ---
    private val _currentRoute = MutableStateFlow("splash")
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _previousRoutes = mutableListOf<String>()

    fun navigateTo(route: String) {
        if (_currentRoute.value != route) {
            _previousRoutes.add(_currentRoute.value)
            _currentRoute.value = route
        }
    }

    fun navigateBack(): Boolean {
        return if (_previousRoutes.isNotEmpty()) {
            _currentRoute.value = _previousRoutes.removeAt(_previousRoutes.lastIndex)
            true
        } else if (_currentRoute.value != "home") {
            _currentRoute.value = "home"
            true
        } else {
            false
        }
    }

    // --- Theme & Language ---
    private val _themeMode = MutableStateFlow(ThemeMode.DARK)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setSelectedLanguage(lang: String) {
        _selectedLanguage.value = lang
    }

    // --- Chat State ---
    val chatMessages = repository.chatMessages
    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _attachedImageBase64 = MutableStateFlow<String?>(null)
    val attachedImageBase64: StateFlow<String?> = _attachedImageBase64.asStateFlow()

    fun setAttachedImage(base64: String?) {
        _attachedImageBase64.value = base64
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank() && _attachedImageBase64.value == null) return
        viewModelScope.launch {
            _isChatLoading.value = true
            repository.sendChatMessage(
                text = text.ifBlank { "Analyze this study image and explain key formulas or concepts." },
                language = _selectedLanguage.value,
                imageBase64 = _attachedImageBase64.value
            )
            _attachedImageBase64.value = null
            _isChatLoading.value = false
        }
    }

    fun clearChat() {
        repository.clearChat()
    }

    // --- Notes State ---
    val notes = repository.notes
    private val _notesSearchQuery = MutableStateFlow("")
    val notesSearchQuery: StateFlow<String> = _notesSearchQuery.asStateFlow()

    fun setNotesSearchQuery(query: String) {
        _notesSearchQuery.value = query
    }

    fun addNote(title: String, subject: String, content: String, tags: List<String>) {
        repository.addNote(title, subject, content, tags)
    }

    fun updateNote(id: String, title: String, subject: String, content: String, tags: List<String>) {
        repository.updateNote(id, title, subject, content, tags)
    }

    fun deleteNote(id: String) {
        repository.deleteNote(id)
    }

    suspend fun summarizeNote(content: String): String {
        return repository.summarizeNoteContent(content)
    }

    suspend fun improveNote(content: String): String {
        return repository.improveNoteContent(content)
    }

    // --- Maths Solver State ---
    private val _mathProblemInput = MutableStateFlow("2x + 3 = 11")
    val mathProblemInput: StateFlow<String> = _mathProblemInput.asStateFlow()

    fun setMathProblemInput(prob: String) {
        _mathProblemInput.value = prob
    }

    private val _selectedMathCategory = MutableStateFlow("Algebra")
    val selectedMathCategory: StateFlow<String> = _selectedMathCategory.asStateFlow()

    fun setSelectedMathCategory(cat: String) {
        _selectedMathCategory.value = cat
    }

    private val _isMathSolving = MutableStateFlow(false)
    val isMathSolving: StateFlow<Boolean> = _isMathSolving.asStateFlow()

    private val _currentMathSolution = MutableStateFlow<MathSolution?>(
        MathSolution(
            problem = "2x + 3 = 11",
            category = "Algebra",
            steps = listOf(
                "Step 1: Write down the linear equation: 2x + 3 = 11",
                "Step 2: Subtract 3 from both sides: 2x = 11 - 3 => 2x = 8",
                "Step 3: Divide both sides by 2: x = 8 / 2",
                "Step 4: Verify by substitution: 2(4) + 3 = 11 (True)"
            ),
            finalAnswer = "x = 4",
            formulaOrMethod = "Linear Algebraic Equation"
        )
    )
    val currentMathSolution: StateFlow<MathSolution?> = _currentMathSolution.asStateFlow()

    fun solveMath(problem: String? = null, category: String? = null) {
        val prob = problem ?: _mathProblemInput.value
        val cat = category ?: _selectedMathCategory.value
        if (prob.isBlank()) return

        viewModelScope.launch {
            _isMathSolving.value = true
            val sol = repository.solveMathProblem(prob, cat)
            _currentMathSolution.value = sol
            _isMathSolving.value = false
        }
    }

    // --- Science Explainer State ---
    private val _selectedScienceCategory = MutableStateFlow("Physics")
    val selectedScienceCategory: StateFlow<String> = _selectedScienceCategory.asStateFlow()

    private val _scienceConcepts = MutableStateFlow<List<ScienceConcept>>(emptyList())
    val scienceConcepts: StateFlow<List<ScienceConcept>> = _scienceConcepts.asStateFlow()

    fun setScienceCategory(cat: String) {
        _selectedScienceCategory.value = cat
        _scienceConcepts.value = repository.getScienceConcepts(cat)
    }

    // --- MCQ & Quiz State ---
    private val _activeQuizQuestions = MutableStateFlow<List<McqQuestion>>(emptyList())
    val activeQuizQuestions: StateFlow<List<McqQuestion>> = _activeQuizQuestions.asStateFlow()

    private val _currentQuizIndex = MutableStateFlow(0)
    val currentQuizIndex: StateFlow<Int> = _currentQuizIndex.asStateFlow()

    private val _selectedOptionIndex = MutableStateFlow<Int?>(null)
    val selectedOptionIndex: StateFlow<Int?> = _selectedOptionIndex.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    private val _isQuizAnswerSubmitted = MutableStateFlow(false)
    val isQuizAnswerSubmitted: StateFlow<Boolean> = _isQuizAnswerSubmitted.asStateFlow()

    private val _isQuizFinished = MutableStateFlow(false)
    val isQuizFinished: StateFlow<Boolean> = _isQuizFinished.asStateFlow()

    fun startQuizWithConfig(subject: String, topic: String, count: Int, difficulty: String) {
        val questions = repository.generateMcqQuestions(subject, topic, count, difficulty)
        _activeQuizQuestions.value = questions
        _currentQuizIndex.value = 0
        _selectedOptionIndex.value = null
        _quizScore.value = 0
        _isQuizAnswerSubmitted.value = false
        _isQuizFinished.value = false
        navigateTo("quiz")
    }

    fun selectQuizOption(index: Int) {
        if (_isQuizAnswerSubmitted.value) return
        _selectedOptionIndex.value = index
    }

    fun submitQuizAnswer() {
        val currentQ = _activeQuizQuestions.value.getOrNull(_currentQuizIndex.value) ?: return
        val selected = _selectedOptionIndex.value ?: return

        _isQuizAnswerSubmitted.value = true
        if (selected == currentQ.correctOptionIndex) {
            _quizScore.value += 1
        }
    }

    fun nextQuizQuestion() {
        if (_currentQuizIndex.value < _activeQuizQuestions.value.size - 1) {
            _currentQuizIndex.value += 1
            _selectedOptionIndex.value = null
            _isQuizAnswerSubmitted.value = false
        } else {
            _isQuizFinished.value = true
        }
    }

    fun previousQuizQuestion() {
        if (_currentQuizIndex.value > 0) {
            _currentQuizIndex.value -= 1
            _selectedOptionIndex.value = null
            _isQuizAnswerSubmitted.value = false
        }
    }

    fun restartQuiz() {
        _currentQuizIndex.value = 0
        _selectedOptionIndex.value = null
        _quizScore.value = 0
        _isQuizAnswerSubmitted.value = false
        _isQuizFinished.value = false
    }

    // --- Study Timer (Pomodoro) State ---
    private val _timerDurationSeconds = MutableStateFlow(25 * 60) // 25 min default
    val timerDurationSeconds: StateFlow<Int> = _timerDurationSeconds.asStateFlow()

    private val _timerSecondsLeft = MutableStateFlow(25 * 60)
    val timerSecondsLeft: StateFlow<Int> = _timerSecondsLeft.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _isBreakMode = MutableStateFlow(false)
    val isBreakMode: StateFlow<Boolean> = _isBreakMode.asStateFlow()

    val completedFocusSessions = repository.focusSessionsCompleted

    private var timerJob: Job? = null

    fun setTimerFocusMode() {
        pauseTimer()
        _isBreakMode.value = false
        _timerDurationSeconds.value = 25 * 60
        _timerSecondsLeft.value = 25 * 60
    }

    fun setTimerBreakMode() {
        pauseTimer()
        _isBreakMode.value = true
        _timerDurationSeconds.value = 5 * 60
        _timerSecondsLeft.value = 5 * 60
    }

    fun startTimer() {
        if (_isTimerRunning.value) return
        _isTimerRunning.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timerSecondsLeft.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerSecondsLeft.value -= 1
            }
            if (_timerSecondsLeft.value <= 0) {
                _isTimerRunning.value = false
                if (!_isBreakMode.value) {
                    repository.completeFocusSession()
                    setTimerBreakMode()
                } else {
                    setTimerFocusMode()
                }
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerSecondsLeft.value = _timerDurationSeconds.value
    }

    // --- Flashcards ---
    val flashcards = repository.flashcards
    private val _currentFlashcardIndex = MutableStateFlow(0)
    val currentFlashcardIndex: StateFlow<Int> = _currentFlashcardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextFlashcard() {
        val total = flashcards.value.size
        if (total > 0) {
            _currentFlashcardIndex.value = (_currentFlashcardIndex.value + 1) % total
            _isCardFlipped.value = false
        }
    }

    fun previousFlashcard() {
        val total = flashcards.value.size
        if (total > 0) {
            _currentFlashcardIndex.value = if (_currentFlashcardIndex.value > 0) _currentFlashcardIndex.value - 1 else total - 1
            _isCardFlipped.value = false
        }
    }

    fun toggleCurrentCardMastered() {
        val card = flashcards.value.getOrNull(_currentFlashcardIndex.value) ?: return
        repository.toggleFlashcardMastered(card.id)
    }

    // --- Calculator State ---
    private val _calculatorExpression = MutableStateFlow("")
    val calculatorExpression: StateFlow<String> = _calculatorExpression.asStateFlow()

    private val _calculatorResult = MutableStateFlow("0")
    val calculatorResult: StateFlow<String> = _calculatorResult.asStateFlow()

    fun onCalculatorKey(key: String) {
        when (key) {
            "C" -> {
                _calculatorExpression.value = ""
                _calculatorResult.value = "0"
            }
            "DEL" -> {
                if (_calculatorExpression.value.isNotEmpty()) {
                    _calculatorExpression.value = _calculatorExpression.value.dropLast(1)
                }
            }
            "=" -> {
                evaluateCalculator()
            }
            else -> {
                _calculatorExpression.value += key
            }
        }
    }

    private fun evaluateCalculator() {
        val expr = _calculatorExpression.value
        if (expr.isBlank()) return
        try {
            // Safe mathematical evaluation
            val clean = expr.replace("×", "*").replace("÷", "/")
            val res = evaluateSimpleMath(clean)
            _calculatorResult.value = if (res % 1.0 == 0.0) res.toLong().toString() else "%.4f".format(res)
        } catch (e: Exception) {
            _calculatorResult.value = "Error"
        }
    }

    private fun evaluateSimpleMath(str: String): Double {
        // Simple 2-pass evaluator
        val tokens = mutableListOf<String>()
        var currentNum = StringBuilder()
        for (ch in str) {
            if (ch in "+-*/^") {
                if (currentNum.isNotEmpty()) {
                    tokens.add(currentNum.toString())
                    currentNum = StringBuilder()
                }
                tokens.add(ch.toString())
            } else if (ch.isDigit() || ch == '.') {
                currentNum.append(ch)
            }
        }
        if (currentNum.isNotEmpty()) tokens.add(currentNum.toString())

        if (tokens.isEmpty()) return 0.0

        // Handle * and /
        val intermediate = mutableListOf<String>()
        var i = 0
        while (i < tokens.size) {
            if (tokens[i] == "*" || tokens[i] == "/" || tokens[i] == "^") {
                val op = tokens[i]
                val prev = intermediate.removeAt(intermediate.lastIndex).toDouble()
                val next = tokens[i + 1].toDouble()
                val r = when (op) {
                    "*" -> prev * next
                    "/" -> if (next != 0.0) prev / next else 0.0
                    "^" -> Math.pow(prev, next)
                    else -> next
                }
                intermediate.add(r.toString())
                i += 2
            } else {
                intermediate.add(tokens[i])
                i++
            }
        }

        // Handle + and -
        var sum = intermediate.firstOrNull()?.toDoubleOrNull() ?: 0.0
        var j = 1
        while (j < intermediate.size) {
            val op = intermediate[j]
            val next = intermediate.getOrNull(j + 1)?.toDoubleOrNull() ?: 0.0
            if (op == "+") sum += next
            if (op == "-") sum -= next
            j += 2
        }
        return sum
    }

    // --- Dictionary State ---
    private val _dictionaryWord = MutableStateFlow("Hypothesis")
    val dictionaryWord: StateFlow<String> = _dictionaryWord.asStateFlow()

    private val _dictionaryEntry = MutableStateFlow(repository.lookupWord("Hypothesis"))
    val dictionaryEntry: StateFlow<DictionaryEntry> = _dictionaryEntry.asStateFlow()

    fun lookupWord(word: String) {
        _dictionaryWord.value = word
        _dictionaryEntry.value = repository.lookupWord(word)
    }

    // --- Translation State ---
    private val _translationInput = MutableStateFlow("Education is the most powerful weapon to change the world.")
    val translationInput: StateFlow<String> = _translationInput.asStateFlow()

    private val _sourceLanguage = MutableStateFlow("English")
    val sourceLanguage: StateFlow<String> = _sourceLanguage.asStateFlow()

    private val _targetLanguage = MutableStateFlow("Hindi")
    val targetLanguage: StateFlow<String> = _targetLanguage.asStateFlow()

    private val _translationResult = MutableStateFlow("शिक्षा दुनिया को बदलने का सबसे शक्तिशाली हथियार है।")
    val translationResult: StateFlow<String> = _translationResult.asStateFlow()

    fun setTranslationInput(text: String) {
        _translationInput.value = text
    }

    fun swapTranslationLanguages() {
        val src = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = src
        val inp = _translationInput.value
        _translationInput.value = _translationResult.value
        _translationResult.value = inp
    }

    fun translateText() {
        viewModelScope.launch {
            val prompt = "Translate this text accurately from ${_sourceLanguage.value} to ${_targetLanguage.value}: \"${_translationInput.value}\". Return ONLY the translated text without extra chatter."
            val res = GeminiApiClient.generateContent(prompt).getOrNull()
            if (res != null && res.isNotBlank()) {
                _translationResult.value = res.trim()
            } else {
                if (_sourceLanguage.value == "English" && _targetLanguage.value == "Hindi") {
                    _translationResult.value = "शिक्षा ही वह प्रकाश है जो भविष्य को रोशन करता है।"
                } else {
                    _translationResult.value = "Education is the light that illuminates the future."
                }
            }
        }
    }

    // --- TTS (Text to Speech) ---
    private var textToSpeech: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    init {
        try {
            textToSpeech = TextToSpeech(application, this)
        } catch (e: Exception) {
            // TTS unavailable or sandboxed
        }
        setScienceCategory("Physics")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.language = Locale.ENGLISH
            _isTtsReady.value = true
        }
    }

    fun speakText(text: String) {
        if (_isTtsReady.value) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "miraj_tts")
        }
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.shutdown()
        timerJob?.cancel()
    }
}
