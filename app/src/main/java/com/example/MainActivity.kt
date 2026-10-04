package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.MirajBottomNav
import com.example.ui.screens.AiPhotoScreen
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.DictionaryScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MathsSolverScreen
import com.example.ui.screens.McqGeneratorScreen
import com.example.ui.screens.NotesMakerScreen
import com.example.ui.screens.OcrScannerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ScienceExplainerScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StudyHubScreen
import com.example.ui.screens.StudyTimerScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.TranslationScreen
import com.example.ui.screens.UnitConverterScreen
import com.example.ui.screens.VoiceAssistantScreen
import com.example.ui.theme.MirajAiTheme
import com.example.ui.theme.MirajDarkBg
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            MirajAiTheme(themeMode = themeMode) {
                MirajApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MirajApp(viewModel: MainViewModel) {
    val currentRoute by viewModel.currentRoute.collectAsState()

    // Handle system back navigation
    if (currentRoute != "home" && currentRoute != "splash") {
        BackHandler {
            viewModel.navigateBack()
        }
    }

    val showBottomBar = currentRoute in listOf("home", "chat", "study", "tools", "profile")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                MirajBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route -> viewModel.navigateTo(route) }
                )
            }
        },
        containerColor = MirajDarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MirajDarkBg)
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentRoute,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { route ->
                when (route) {
                    "splash" -> SplashScreen(
                        onSplashFinished = { viewModel.navigateTo("home") }
                    )
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    "chat" -> ChatScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    "study" -> StudyHubScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    "maths" -> MathsSolverScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "science" -> ScienceExplainerScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() },
                        onAskInChat = { question ->
                            viewModel.sendChatMessage(question)
                            viewModel.navigateTo("chat")
                        }
                    )
                    "notes" -> NotesMakerScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "mcq" -> McqGeneratorScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "quiz" -> QuizScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "timer" -> StudyTimerScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "flashcards" -> FlashcardsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "tools" -> ToolsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() },
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                    "calculator" -> CalculatorScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "converter" -> UnitConverterScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "ocr" -> OcrScannerScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() },
                        onSendToChat = { text ->
                            viewModel.sendChatMessage("Analyze and explain this text:\n$text")
                            viewModel.navigateTo("chat")
                        },
                        onSaveToNotes = { text ->
                            viewModel.addNote("Scanned Textbook Notes", "General Science", text, listOf("OCR", "Textbook"))
                            viewModel.navigateTo("notes")
                        }
                    )
                    "dictionary" -> DictionaryScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "translation" -> TranslationScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "photo" -> AiPhotoScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "voice" -> VoiceAssistantScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    "profile" -> ProfileScreen(
                        viewModel = viewModel,
                        onNavigateBack = { viewModel.navigateBack() }
                    )
                    else -> HomeScreen(
                        viewModel = viewModel,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }
            }
        }
    }
}
