package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    onAddClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Neuen Ort hinzufügen")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Meine Happy Places", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            if (happyPlaces.isEmpty()) {
                // Leerer Zustand
                Text(
                    "Es sind aktuell keine Happy Places gespeichert.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn {
                    items(happyPlaces.size) { idx ->
                        val place = happyPlaces[idx]
                        Text(place.name) // Passe dies an dein UI-Design an
                    }
                }
            }
        }
    }
}

// Dummy-Datenklasse, ersetze sie durch deine eigene!
data class HappyPlace(val name: String)