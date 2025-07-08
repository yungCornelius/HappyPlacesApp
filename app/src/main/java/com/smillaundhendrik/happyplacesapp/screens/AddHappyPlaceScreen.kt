package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHappyPlaceScreen(
    navController: NavController,
    tempLat: Double?,
    tempLon: Double?,
    onLocationReset: () -> Unit,
    onSave: (String, String, Double, Double) -> Unit,
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    // Übernehme Koordinaten von der Karte (einmalig)
    LaunchedEffect(tempLat, tempLon) {
        if (tempLat != null && tempLon != null) {
            latitude = tempLat
            longitude = tempLon
            onLocationReset()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neuen Happy Place hinzufügen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Beschreibung") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Standort auswählen", style = MaterialTheme.typography.titleMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        // TODO: Aktuellen Standort übernehmen
                        // latitude = ...
                        // longitude = ...
                        error = "Noch nicht implementiert."
                    }
                ) {
                    Text("Aktuellen Standort übernehmen")
                }
                Button(
                    onClick = { navController.navigate("pickLocation") }
                ) {
                    Text("Auf Karte wählen")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (latitude != null && longitude != null) {
                Text("Gewählter Standort: $latitude, $longitude")
            } else {
                Text("Kein Standort gewählt", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (name.isBlank() || latitude == null || longitude == null) {
                        error = "Bitte Name und Standort angeben."
                    } else {
                        onSave(name, description, latitude!!, longitude!!)
                        onNavigateBack()
                    }
                },
                enabled = name.isNotBlank() && latitude != null && longitude != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Happy Place speichern")
            }

        }
    }
}