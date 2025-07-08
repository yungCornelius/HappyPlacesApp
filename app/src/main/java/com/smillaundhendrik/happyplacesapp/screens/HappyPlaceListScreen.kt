package com.smillaundhendrik.happyplacesapp.screens

import ads_mobile_sdk.h6
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    modifier: Modifier = Modifier,
    onPlaceClick: (HappyPlace) -> Unit = {},
    onDeleteClick: (HappyPlace) -> Unit = {},
    onAddClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (happyPlaces.isEmpty()) {
            Text(
                "Noch keine Happy Places gespeichert.",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
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
                                    Text(place.name, style = MaterialTheme.typography.headlineSmall)
                                    if (place.beschreibung.isNotBlank()) {
                                        Text(place.beschreibung, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    Text("Standort: ${place.latitude}, ${place.longitude}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    )
                }
            }
        }

        // Optional: Leerer Zustand
        if (happyPlaces.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text("Noch keine Happy Places gespeichert.")
            }
        }
    }
}