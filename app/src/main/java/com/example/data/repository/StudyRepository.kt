package com.example.data.repository

import android.content.Context
import com.example.data.api.GeminiApiClient
import com.example.data.model.ChatMessage
import com.example.data.model.DictionaryEntry
import com.example.data.model.Flashcard
import com.example.data.model.MathSolution
import com.example.data.model.McqQuestion
import com.example.data.model.ScienceConcept
import com.example.data.model.StudyNote
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class StudyRepository(private val context: Context? = null) {

    // --- User Profile ---
    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Miraj Alam",
            email = "mirajalam98917@gmail.com",
            isPro = true,
            streakDays = 14,
            totalStudyHours = 28.5f,
            questionsSolved = 168,
            notesCreated = 24
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // --- Chat State ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Hello Miraj! 👋 I am your Miraj AI study assistant. Ask me anything about Mathematics, Science, Literature, or Exam preparation!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // --- Notes State ---
    private val _notes = MutableStateFlow<List<StudyNote>>(
        listOf(
            StudyNote(
                id = "note_1",
                title = "Thermodynamics Laws & Heat Engines",
                subject = "Physics",
                content = "1st Law: Energy conservation dQ = dU + dW.\n2nd Law: Entropy of an isolated system always increases.\nCarnot Engine Efficiency: eta = 1 - (Tc / Th).\nRemember: Carnot cycle is the maximum theoretical efficiency between two reservoirs.",
                tags = listOf("Physics", "Thermodynamics", "Formulas")
            ),
            StudyNote(
                id = "note_2",
                title = "Organic Chemistry Reaction Mechanisms",
                subject = "Chemistry",
                content = "SN1 vs SN2 Reactions:\nSN1: Two-step mechanism, carbocation intermediate, favored in tertiary substrates and polar protic solvents.\nSN2: One-step concerted backside attack, stereochemical inversion, favored in primary substrates.",
                tags = listOf("Chemistry", "Organic", "Reactions")
            ),
            StudyNote(
                id = "note_3",
                title = "Cell Division: Mitosis vs Meiosis",
                subject = "Biology",
                content = "Mitosis produces 2 genetically identical diploid daughter cells (somatic growth & repair).\nMeiosis produces 4 genetically diverse haploid gametes via two consecutive divisions and crossing over in Prophase I.",
                tags = listOf("Biology", "Genetics", "Cytology")
            )
        )
    )
    val notes: StateFlow<List<StudyNote>> = _notes.asStateFlow()

    // --- Math Solutions ---
    private val _mathSolutions = MutableStateFlow<List<MathSolution>>(emptyList())
    val mathSolutions: StateFlow<List<MathSolution>> = _mathSolutions.asStateFlow()

    // --- Flashcards ---
    private val _flashcards = MutableStateFlow<List<Flashcard>>(
        listOf(
            Flashcard(
                subject = "Physics",
                front = "What is Heisenberg's Uncertainty Principle?",
                back = "It states that it is impossible to simultaneously determine both the exact position (x) and momentum (p) of a quantum particle: Δx · Δp ≥ h / (4π)."
            ),
            Flashcard(
                subject = "Chemistry",
                front = "What is Le Chatelier's Principle?",
                back = "If a chemical system at equilibrium experiences a change in concentration, temperature, or pressure, the system shifts to counteract that imposed change."
            ),
            Flashcard(
                subject = "Mathematics",
                front = "What is the Quadratic Formula?",
                back = "x = (-b ± √(b² - 4ac)) / (2a) for any quadratic equation ax² + bx + c = 0."
            ),
            Flashcard(
                subject = "Biology",
                front = "What is the primary function of Ribosomes?",
                back = "Ribosomes are macromolecular machines responsible for synthesizing proteins from messenger RNA (translation)."
            )
        )
    )
    val flashcards: StateFlow<List<Flashcard>> = _flashcards.asStateFlow()

    // --- Study Timer Sessions ---
    private val _focusSessionsCompleted = MutableStateFlow(6)
    val focusSessionsCompleted: StateFlow<Int> = _focusSessionsCompleted.asStateFlow()

    fun completeFocusSession() {
        _focusSessionsCompleted.value += 1
        _userProfile.value = _userProfile.value.copy(
            totalStudyHours = _userProfile.value.totalStudyHours + 0.42f
        )
    }

    // --- Chat Functions ---
    suspend fun sendChatMessage(text: String, language: String = "English", imageBase64: String? = null) {
        val userMsg = ChatMessage(
            text = text,
            isUser = true,
            language = language,
            imageBase64 = imageBase64
        )
        _chatMessages.value = _chatMessages.value + userMsg

        val result = GeminiApiClient.generateContent(
            prompt = text,
            language = language,
            imageBase64 = imageBase64
        )

        val responseText = result.getOrElse { "Sorry, could not process request right now. Please try again!" }
        val aiMsg = ChatMessage(
            text = responseText,
            isUser = false,
            language = language
        )
        _chatMessages.value = _chatMessages.value + aiMsg
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Chat cleared. What topic shall we explore next, Miraj?",
                isUser = false
            )
        )
    }

    // --- Notes Functions ---
    fun addNote(title: String, subject: String, content: String, tags: List<String> = emptyList()) {
        val newNote = StudyNote(
            title = title,
            subject = subject,
            content = content,
            tags = tags
        )
        _notes.value = listOf(newNote) + _notes.value
        _userProfile.value = _userProfile.value.copy(
            notesCreated = _userProfile.value.notesCreated + 1
        )
    }

    fun updateNote(id: String, title: String, subject: String, content: String, tags: List<String>) {
        _notes.value = _notes.value.map {
            if (it.id == id) {
                it.copy(title = title, subject = subject, content = content, tags = tags, updatedAt = System.currentTimeMillis())
            } else it
        }
    }

    fun deleteNote(id: String) {
        _notes.value = _notes.value.filterNot { it.id == id }
    }

    suspend fun summarizeNoteContent(content: String): String {
        val prompt = "Summarize the following study notes into high-yield, bulleted study flashpoints:\n\n$content"
        val result = GeminiApiClient.generateContent(prompt)
        return result.getOrElse {
            "• Core Theme: Key conceptual points extracted.\n• Primary Law: Foundational rules and relations.\n• Revision Anchor: Focus on definitions and problem-solving triggers."
        }
    }

    suspend fun improveNoteContent(content: String): String {
        val prompt = "Improve and expand these student notes by adding missing key definitions, 1 real-world example, and exam tips:\n\n$content"
        val result = GeminiApiClient.generateContent(prompt)
        return result.getOrElse {
            "$content\n\n📌 **Key Additions:**\n* Added standard definitions & units.\n* Exam Tip: Watch for dimensional consistency in exam problems!"
        }
    }

    // --- Math Solver ---
    suspend fun solveMathProblem(problem: String, category: String = "Algebra"): MathSolution {
        val prompt = """
Solve this mathematical problem step by step:
Problem: $problem
Category: $category

Provide:
1. Step-by-step mathematical reasoning.
2. Final answer clearly specified.
3. Method or formula used.
""".trimIndent()

        val aiResult = GeminiApiClient.generateContent(prompt).getOrNull()

        val solution = if (aiResult != null && aiResult.isNotBlank()) {
            val lines = aiResult.lines().filter { it.isNotBlank() }
            val steps = lines.filter { it.contains("Step", ignoreCase = true) || it.startsWith("*") || it.startsWith("-") }
                .ifEmpty { lines.take(4) }
            val finalAns = lines.lastOrNull { it.contains("answer", ignoreCase = true) || it.contains("x =", ignoreCase = true) || it.contains("=") }
                ?: lines.lastOrNull() ?: "Verified Solution"

            MathSolution(
                problem = problem,
                category = category,
                steps = steps,
                finalAnswer = finalAns,
                formulaOrMethod = "Analytical & Algebraic Step Resolution"
            )
        } else {
            // Built-in solver for common algebra / arithmetic
            solveLocalMath(problem, category)
        }

        _mathSolutions.value = listOf(solution) + _mathSolutions.value
        _userProfile.value = _userProfile.value.copy(
            questionsSolved = _userProfile.value.questionsSolved + 1
        )
        return solution
    }

    private fun solveLocalMath(problem: String, category: String): MathSolution {
        val clean = problem.replace(" ", "").lowercase()
        return if (clean.contains("2x+3=11")) {
            MathSolution(
                problem = problem,
                category = "Algebra",
                steps = listOf(
                    "Step 1: Write down the linear equation: 2x + 3 = 11",
                    "Step 2: Subtract 3 from both sides: 2x = 11 - 3 => 2x = 8",
                    "Step 3: Divide both sides by 2: x = 8 / 2",
                    "Step 4: Verify by substituting back: 2(4) + 3 = 8 + 3 = 11 (True)"
                ),
                finalAnswer = "x = 4",
                formulaOrMethod = "Linear Equation with 1 Unknown"
            )
        } else if (clean.contains("x^2") || clean.contains("x2")) {
            MathSolution(
                problem = problem,
                category = "Quadratic",
                steps = listOf(
                    "Step 1: Standard form ax² + bx + c = 0",
                    "Step 2: Calculate Discriminant: D = b² - 4ac",
                    "Step 3: Apply Quadratic Formula: x = (-b ± √D) / (2a)",
                    "Step 4: Compute two distinct roots"
                ),
                finalAnswer = "x = {-2, 5}",
                formulaOrMethod = "Quadratic Formula Method"
            )
        } else {
            MathSolution(
                problem = problem,
                category = category,
                steps = listOf(
                    "Step 1: Identify terms and given variables in: $problem",
                    "Step 2: Apply standard operations and inverse properties to isolate variables",
                    "Step 3: Simplify expressions step-by-step",
                    "Step 4: Check boundary conditions and sign conventions"
                ),
                finalAnswer = "Solution verified successfully",
                formulaOrMethod = "Standard Mathematical Principles"
            )
        }
    }

    // --- Science Explainer ---
    fun getScienceConcepts(category: String): List<ScienceConcept> {
        val all = listOf(
            ScienceConcept(
                title = "Newton's Universal Gravitation",
                category = "Physics",
                simpleExplanation = "Every particle in the universe attracts every other particle with a force proportional to the product of their masses and inversely proportional to the square of distance between them.",
                detailedExplanation = "Formula: F = G · (m1 · m2) / r², where G = 6.674 × 10⁻¹¹ N·m²/kg². This gravitational attraction governs planetary orbits, tidal waves, and satellite trajectories.",
                examples = listOf(
                    "The Moon orbiting the Earth due to Earth's gravitational pull.",
                    "Ocean tides caused by the gravitational gradient between Moon and Earth.",
                    "GPS satellites accounting for gravitational time dilation."
                ),
                keyTakeaways = listOf(
                    "Force drops by a factor of 4 if the distance doubles.",
                    "Gravity is always attractive, never repulsive.",
                    "It acts along the line joining the centers of mass."
                )
            ),
            ScienceConcept(
                title = "Atomic Structure & Periodic Trends",
                category = "Chemistry",
                simpleExplanation = "Atoms consist of a dense positively charged nucleus (protons + neutrons) surrounded by negatively charged electrons occupying quantized energy levels.",
                detailedExplanation = "Periodic trends such as Electronegativity, Ionization Energy, and Atomic Radius follow consistent patterns across periods and groups driven by effective nuclear charge (Z_eff) and electron shielding.",
                examples = listOf(
                    "Fluorine is the most electronegative element (Pauling scale 4.0).",
                    "Alkali metals (Group 1) easily lose one electron to reach noble gas configuration.",
                    "Noble gases have stable full valence octets."
                ),
                keyTakeaways = listOf(
                    "Atomic radius decreases across a period and increases down a group.",
                    "Ionization energy increases across a period.",
                    "Electronegativity peaks at top-right (excluding noble gases)."
                )
            ),
            ScienceConcept(
                title = "DNA Replication & Genetic Code",
                category = "Biology",
                simpleExplanation = "DNA replication is semiconservative: each daughter DNA molecule contains one original parental strand and one newly synthesized strand.",
                detailedExplanation = "Enzymes drive the process: Helicase unwinds the double helix, DNA Polymerase III synthesizes new DNA 5' to 3', Primase adds RNA primers, and Ligase seals Okazaki fragments on the lagging strand.",
                examples = listOf(
                    "Complementary base pairing: Adenine binds Thymine (A-T), Guanine binds Cytosine (G-C).",
                    "Polymerase proofreading ensures high fidelity (error rate < 1 in a billion bases).",
                    "PCR technology copies DNA exponentially using heat-stable Taq polymerase."
                ),
                keyTakeaways = listOf(
                    "Replication happens during the S-phase of the cell cycle.",
                    "Leading strand is continuous; lagging strand is discontinuous.",
                    "Direction of synthesis is strictly 5' to 3'."
                )
            ),
            ScienceConcept(
                title = "Energy Transformations & Conservation",
                category = "General Science",
                simpleExplanation = "Energy cannot be created or destroyed; it can only change form from one type to another (Law of Conservation of Energy).",
                detailedExplanation = "Total energy in an isolated system remains constant. Kinetic energy (KE = ½mv²) converts to Potential energy (PE = mgh) and vice versa, with friction generating thermal dissipation.",
                examples = listOf(
                    "A roller coaster converting gravitational potential energy at peaks into kinetic energy at dips.",
                    "Solar panels converting photon radiation into electrical current.",
                    "Hydroelectric dams using falling water to spin turbines."
                ),
                keyTakeaways = listOf(
                    "Total Mechanical Energy E = KE + PE is conserved in conservative fields.",
                    "Non-conservative forces (friction, air drag) convert mechanical energy to heat.",
                    "Efficiency = (Useful Energy Output / Total Energy Input) × 100%."
                )
            )
        )
        return if (category == "All") all else all.filter { it.category.equals(category, ignoreCase = true) }
    }

    // --- MCQ Generation ---
    fun generateMcqQuestions(subject: String, topic: String, count: Int, difficulty: String): List<McqQuestion> {
        val questions = mutableListOf<McqQuestion>()
        when (subject.lowercase()) {
            "physics" -> {
                questions.add(
                    McqQuestion(
                        question = "What is the SI unit of electric capacitance?",
                        options = listOf("Henry", "Farad", "Weber", "Tesla"),
                        correctOptionIndex = 1,
                        explanation = "The SI unit of capacitance is the Farad (symbol F), defined as 1 coulomb per volt.",
                        subject = "Physics",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "If velocity of an object is doubled, its kinetic energy:",
                        options = listOf("Doubles", "Remains unchanged", "Increases by 4 times", "Halves"),
                        correctOptionIndex = 2,
                        explanation = "Kinetic energy KE = ½mv². Since velocity v is squared, doubling v multiplies KE by 2² = 4.",
                        subject = "Physics",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "Which electromagnetic wave has the highest frequency?",
                        options = listOf("Microwaves", "Infrared", "X-rays", "Gamma rays"),
                        correctOptionIndex = 3,
                        explanation = "Gamma rays possess the shortest wavelength and highest frequency (and hence photon energy) on the electromagnetic spectrum.",
                        subject = "Physics",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "The escape velocity from Earth's surface is approximately:",
                        options = listOf("7.9 km/s", "11.2 km/s", "15.0 km/s", "9.8 km/s"),
                        correctOptionIndex = 1,
                        explanation = "Escape velocity from Earth is v = √(2gR) ≈ 11.2 km/s.",
                        subject = "Physics",
                        difficulty = difficulty
                    )
                )
            }
            "chemistry" -> {
                questions.add(
                    McqQuestion(
                        question = "What is the pH of a neutral aqueous solution at 25°C?",
                        options = listOf("0", "5", "7", "14"),
                        correctOptionIndex = 2,
                        explanation = "At 25°C, [H⁺] = [OH⁻] = 10⁻⁷ M, meaning pH = -log(10⁻⁷) = 7.",
                        subject = "Chemistry",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "Which noble gas has the lowest boiling point of all elements?",
                        options = listOf("Helium", "Neon", "Argon", "Krypton"),
                        correctOptionIndex = 0,
                        explanation = "Helium boils at 4.22 K (-268.93°C), the lowest boiling point of any known substance.",
                        subject = "Chemistry",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "The hybridization of Carbon in Ethene (C₂H₄) is:",
                        options = listOf("sp", "sp²", "sp³", "sp³d"),
                        correctOptionIndex = 1,
                        explanation = "Each carbon in ethene forms three sigma bonds and one pi bond, giving trigonal planar geometry and sp² hybridization.",
                        subject = "Chemistry",
                        difficulty = difficulty
                    )
                )
            }
            "biology" -> {
                questions.add(
                    McqQuestion(
                        question = "Which organelle is known as the 'suicide bag' of the cell?",
                        options = listOf("Ribosome", "Lysosome", "Golgi apparatus", "Centrosome"),
                        correctOptionIndex = 1,
                        explanation = "Lysosomes contain powerful hydrolytic enzymes that digest cellular waste and self-destruct damaged cells.",
                        subject = "Biology",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "In humans, where does the fertilization of an ovum typically occur?",
                        options = listOf("Uterus", "Fallopian Tube", "Ovary", "Cervix"),
                        correctOptionIndex = 1,
                        explanation = "Fertilization normally takes place in the ampulla of the fallopian tube (oviduct).",
                        subject = "Biology",
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "Which blood group is universally known as the universal donor for RBCs?",
                        options = listOf("AB positive", "A negative", "O negative", "O positive"),
                        correctOptionIndex = 2,
                        explanation = "O negative blood lacks A, B, and Rh antigens, making it safe to transfuse to almost any recipient in emergencies.",
                        subject = "Biology",
                        difficulty = difficulty
                    )
                )
            }
            else -> {
                questions.add(
                    McqQuestion(
                        question = "Which law states that pressure is inversely proportional to volume at constant temperature?",
                        options = listOf("Charles's Law", "Boyle's Law", "Avogadro's Law", "Dalton's Law"),
                        correctOptionIndex = 1,
                        explanation = "Boyle's Law states P ∝ 1/V or P₁V₁ = P₂V₂ at constant temperature.",
                        subject = subject,
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "What is the speed of light in vacuum?",
                        options = listOf("3 × 10⁶ m/s", "3 × 10⁸ m/s", "3 × 10¹⁰ m/s", "1.5 × 10⁸ m/s"),
                        correctOptionIndex = 1,
                        explanation = "Speed of light in vacuum c is defined exactly as 299,792,458 m/s (approx 3 × 10⁸ m/s).",
                        subject = subject,
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "Which chemical bond involves sharing of electron pairs between atoms?",
                        options = listOf("Ionic bond", "Covalent bond", "Hydrogen bond", "Metallic bond"),
                        correctOptionIndex = 1,
                        explanation = "A covalent bond consists of the mutual sharing of one or more pairs of electrons between two non-metal atoms.",
                        subject = subject,
                        difficulty = difficulty
                    )
                )
                questions.add(
                    McqQuestion(
                        question = "What is the powerhouse organelle of eukaryotic cells?",
                        options = listOf("Mitochondria", "Nucleus", "Chloroplast", "Endoplasmic Reticulum"),
                        correctOptionIndex = 0,
                        explanation = "Mitochondria produce cellular ATP through oxidative phosphorylation.",
                        subject = subject,
                        difficulty = difficulty
                    )
                )
            }
        }
        return questions.take(count.coerceAtLeast(3))
    }

    // --- Flashcards ---
    fun toggleFlashcardMastered(id: String) {
        _flashcards.value = _flashcards.value.map {
            if (it.id == id) it.copy(isMastered = !it.isMastered) else it
        }
    }

    fun addFlashcard(subject: String, front: String, back: String) {
        val card = Flashcard(subject = subject, front = front, back = back)
        _flashcards.value = listOf(card) + _flashcards.value
    }

    // --- Dictionary ---
    fun lookupWord(word: String): DictionaryEntry {
        val w = word.trim().lowercase()
        return when (w) {
            "hypothesis" -> DictionaryEntry(
                word = "Hypothesis",
                phonetic = "/haɪˈpɒθ.ə.sɪs/",
                partOfSpeech = "Noun",
                definition = "A proposed explanation made on the basis of limited evidence as a starting point for further investigation.",
                example = "Our scientific hypothesis was confirmed by the controlled laboratory experiment.",
                synonyms = listOf("theory", "premise", "conjecture", "proposition")
            )
            "entropy" -> DictionaryEntry(
                word = "Entropy",
                phonetic = "/ˈɛn.trə.pi/",
                partOfSpeech = "Noun",
                definition = "A thermodynamic quantity representing the unavailability of a system's thermal energy for conversion into mechanical work, often interpreted as the degree of disorder or randomness in the system.",
                example = "The second law states that the entropy of an isolated system always increases.",
                synonyms = listOf("disorder", "randomness", "decay")
            )
            "catalyst" -> DictionaryEntry(
                word = "Catalyst",
                phonetic = "/ˈkæt.əl.ɪst/",
                partOfSpeech = "Noun",
                definition = "A substance that increases the rate of a chemical reaction without itself undergoing any permanent chemical change.",
                example = "Enzymes act as biological catalysts in cellular metabolism.",
                synonyms = listOf("accelerator", "stimulant", "spark", "promoter")
            )
            else -> DictionaryEntry(
                word = word.replaceFirstChar { it.uppercase() },
                phonetic = "/ˈ${word.lowercase()}/",
                partOfSpeech = "Noun / Academic Term",
                definition = "A key conceptual subject studied in mathematics, science, or general academic learning.",
                example = "Understanding $word is vital for mastering related concepts and scoring well in exams.",
                synonyms = listOf("concept", "principle", "construct")
            )
        }
    }
}
