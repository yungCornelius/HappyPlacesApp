package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smillaundhendrik.happyplacesapp.data.HappyPlace

@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    onAddClick: () -> Unit,
    onDeleteClick: (HappyPlace) -> Unit
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
                Text(
                    "Es sind aktuell keine Happy Places gespeichert.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(happyPlaces) { place ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(place.name, style = MaterialTheme.typography.titleLarge)
                                if (place.beschreibung.isNotBlank()) {
                                    Text(place.beschreibung, style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(
                                    "Standort: ${place.latitude}, ${place.longitude}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}