package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppTab
import com.example.ui.components.CgpaBottomNavBar
import com.example.ui.components.CgpaTopBar
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CgpaTheme
import com.example.ui.viewmodel.CgpaViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CgpaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            CgpaTheme(darkTheme = isDarkMode) {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: CgpaViewModel) {
    var currentTab by remember { mutableStateOf(AppTab.CALCULATOR) }
    val studentName by viewModel.studentName.collectAsState()
    var showNamePrompt by remember(studentName) { mutableStateOf(studentName.isBlank()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CgpaTopBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        bottomBar = {
            CgpaBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (currentTab) {
            AppTab.CALCULATOR -> CalculatorScreen(
                viewModel = viewModel,
                modifier = modifier
            )
            AppTab.HISTORY -> HistoryScreen(
                viewModel = viewModel,
                onNavigateToCalculator = { currentTab = AppTab.CALCULATOR },
                modifier = modifier
            )
            AppTab.SETTINGS -> SettingsScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }

        // Welcome / Name Prompt on First Open
        if (showNamePrompt) {
            var nameInput by remember { mutableStateOf("") }
            var yearInput by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_icon_fg),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Welcome to CGPA Calculator",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Please enter your name to personalize your grade tracker:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Your Name") },
                            placeholder = { Text("e.g. John Doe") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = yearInput,
                            onValueChange = { yearInput = it },
                            label = { Text("Program / Year (Optional)") },
                            placeholder = { Text("e.g. Computer Science") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val finalName = if (nameInput.isBlank()) "Student" else nameInput.trim()
                            val finalYear = yearInput.trim()
                            viewModel.updateProfile(finalName, finalYear)
                            showNamePrompt = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        enabled = nameInput.isNotBlank()
                    ) {
                        Text("Get Started")
                    }
                }
            )
        }
    }
}
