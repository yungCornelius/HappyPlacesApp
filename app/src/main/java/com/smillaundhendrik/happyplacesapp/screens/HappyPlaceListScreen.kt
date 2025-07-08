package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smillaundhendrik.happyplacesapp.data.HappyPlace

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    onAddClick: () -> Unit,
    onPlaceClick: (HappyPlace) -> Unit,
    onDeleteClick: (HappyPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Die Liste
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = happyPlaces,
                key = { it.id }
            ) { place ->
                var isDismissed by remember { mutableStateOf(false) }
                if (!isDismissed) {
                    val dismissState = rememberDismissState(
                        confirmStateChange = { value ->
                            if (value == DismissValue.DismissedToEnd || value == DismissValue.DismissedToStart) {
                                isDismissed = true
                                onDeleteClick(place)
                                true
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(
                            DismissDirection.EndToStart,
                            DismissDirection.StartToEnd
                        ),
                        background = {
                            val color = when (dismissState.dismissDirection) {
                                DismissDirection.StartToEnd, DismissDirection.EndToStart -> Color.Red
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Löschen",
                                    tint = Color.White
                                )
                            }
                        },
                        dismissContent = {
                            Card(
                                onClick = { onPlaceClick(place) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(place.name, style = MaterialTheme.typography.h6)
                                    if (place.beschreibung.isNotBlank()) {
                                        Text(place.beschreibung, style = MaterialTheme.typography.body2)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        // Der Platzhalter wird als Overlay angezeigt, sobald die Liste leer ist
        if (happyPlaces.isEmpty()) {
            // Overlay-Box füllt die gesamte Fläche und zeigt den Hinweis zentriert
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent), // Optional: halbtransparent für den Overlay-Effekt
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(24.dp),
                    elevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Noch keine Happy Places gespeichert.",
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Mit + kannst du einen neuen Ort hinzufügen.",
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}