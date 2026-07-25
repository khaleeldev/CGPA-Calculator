package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.ui.viewmodel.CgpaViewModel

@Composable
fun SettingsScreen(
    viewModel: CgpaViewModel,
    modifier: Modifier = Modifier
) {
    val studentName by viewModel.studentName.collectAsState()
    val academicYear by viewModel.academicYear.collectAsState()
    val gradingScale by viewModel.gradingScale.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    var showProfileDialog by remember { mutableStateOf(false) }
    var showScaleDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Title Header
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Manage your preferences and app details.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            // Student Profile Entry
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showProfileDialog = true }
                        .testTag("settings_profile_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Student Profile Icon",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                if (studentName.isNotBlank() || academicYear.isNotBlank()) {
                                    Text(
                                        text = studentName.ifBlank { "Student" },
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    if (academicYear.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = academicYear,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "Set Your Name",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Tap to configure student profile",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            // Settings Group Container Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Item 1: Grading Scale
                        SettingsItemRow(
                            icon = Icons.Default.Grade,
                            title = "Grading Scale",
                            subtitle = "Current: ${gradingScale} Scale",
                            trailing = {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${gradingScale}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            },
                            onClick = { showScaleDialog = true }
                        )

                        DividerLine()

                        // Item 2: Dark Mode
                        SettingsItemRow(
                            icon = Icons.Default.DarkMode,
                            title = "Dark Mode",
                            subtitle = "Reduce eye strain at night",
                            trailing = {
                                Switch(
                                    checked = isDarkMode,
                                    onCheckedChange = { viewModel.toggleDarkMode(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            },
                            onClick = { viewModel.toggleDarkMode(!isDarkMode) }
                        )

                        DividerLine()

                        // Item 3: About the App
                        SettingsItemRow(
                            icon = Icons.Default.Info,
                            title = "About the App",
                            subtitle = "Version 1.0.0",
                            trailing = {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "About App",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            },
                            onClick = { showAboutDialog = true }
                        )

                        DividerLine()

                        // Item 4: Privacy Policy
                        SettingsItemRow(
                            icon = Icons.Default.VerifiedUser,
                            title = "Privacy Policy",
                            subtitle = "How we handle your academic data",
                            trailing = {
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Privacy Policy",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            },
                            onClick = { showPrivacyDialog = true }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Dialog 1: Profile Editor
        if (showProfileDialog) {
            var nameInput by remember { mutableStateOf(studentName) }
            var yearInput by remember { mutableStateOf(academicYear) }

            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                title = { Text("Student Profile", style = MaterialTheme.typography.headlineMedium) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Student Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = yearInput,
                            onValueChange = { yearInput = it },
                            label = { Text("Program / Year (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateProfile(nameInput, yearInput)
                            showProfileDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showProfileDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Dialog 2: Grading Scale Picker
        if (showScaleDialog) {
            var selectedScale by remember { mutableStateOf(gradingScale) }

            AlertDialog(
                onDismissRequest = { showScaleDialog = false },
                title = { Text("Select Grading Scale", style = MaterialTheme.typography.headlineMedium) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedScale = 5.0 }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (selectedScale == 5.0),
                                onClick = { selectedScale = 5.0 }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("5.0 Grading Scale", style = MaterialTheme.typography.bodyLarge)
                                Text("A=5, B=4, C=3, D=2, E=1, F=0", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedScale = 4.0 }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (selectedScale == 4.0),
                                onClick = { selectedScale = 4.0 }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("4.0 Grading Scale", style = MaterialTheme.typography.bodyLarge)
                                Text("A=4, B=3, C=2, D=1, F=0", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateGradingScale(selectedScale)
                            showScaleDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Apply")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showScaleDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Dialog 3: About App
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About CGPA Calculator", style = MaterialTheme.typography.headlineMedium) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Version: 1.0.0", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                        Text(
                            "CGPA Calculator is a lightweight, academic tool designed to help students keep track of semester grades, calculate term CGPA accurately, and store historical records offline.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = { showAboutDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }

        // Dialog 4: Privacy Policy
        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                title = { Text("Privacy Policy", style = MaterialTheme.typography.headlineMedium) },
                text = {
                    Text(
                        "We prioritize your privacy. All your course entries, grade calculations, and student profile preferences remain 100% stored locally in your device's Room database. No data is collected or transmitted to external servers.",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                confirmButton = {
                    Button(onClick = { showPrivacyDialog = false }) {
                        Text("I Understand")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        trailing()
    }
}

@Composable
fun DividerLine() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    ) {}
}
