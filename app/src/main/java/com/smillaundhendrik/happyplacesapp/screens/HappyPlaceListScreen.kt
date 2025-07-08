package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smillaundhendrik.happyplacesapp.data.HappyPlace

/**
 * Zeigt die Liste der gespeicherten Happy Places an.
 *
 * Falls die Liste leer ist, wird ein Platzhalter-Text angezeigt.
 *
 * @param happyPlaces Die anzuzeigende Liste von HappyPlace-Objekten
 * @param onAddClick Callback, wenn der Hinzufügen-Button gedrückt wird (wird von FAB übergeben)
 * @param onPlaceClick Callback, wenn ein Eintrag angewählt wird (mit dem jeweiligen HappyPlace)
 * @param modifier Modifier für äußeres Layout/Padding (z.B. innerPadding aus Scaffold)
 */
@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    onAddClick: () -> Unit,
    onPlaceClick: (HappyPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Der Hinzufügen-Button wird jetzt als FAB im Scaffold angezeigt, daher hier entfernt.

        if (happyPlaces.isEmpty()) {
            // Platzhalter, wenn keine Orte vorhanden sind
            Text(
                "Noch keine Happy Places gespeichert.\nMit + kannst du einen neuen Ort hinzufügen.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            // Liste anzeigen
            happyPlaces.forEach { place ->
                Card(
                    onClick = { onPlaceClick(place) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(place.name, style = MaterialTheme.typography.titleMedium)
                        if (place.beschreibung.isNotBlank()) {
                            Text(place.beschreibung, style = MaterialTheme.typography.bodySmall)
                        }
                        // Optional: Zeige weitere Infos, z.B. Notizen, Koordinaten etc.
                    }
                }
            }
        }
    }
}