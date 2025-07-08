package com.smillaundhendrik.happyplacesapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smillaundhendrik.happyplacesapp.data.HappyPlace
import androidx.compose.material3.TopAppBar

/**
 * Haupt-Listenansicht für die gespeicherten Happy Places.
 *
 * @param happyPlaces Liste aller gespeicherten Orte, die angezeigt werden sollen.
 * @param onAddClick Lambda, das ausgelöst wird, wenn der Benutzer auf das "+"-FloatingActionButton klickt.
 * @param onPlaceClick Lambda, das ausgelöst wird, wenn auf einen Listeneintrag (Happy Place) geklickt wird.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HappyPlaceListScreen(
    happyPlaces: List<HappyPlace>,
    onAddClick: () -> Unit,
    onPlaceClick: (HappyPlace) -> Unit
) {
    // Scaffold stellt das Grundgerüst für die Seite bereit, inklusive TopAppBar und FloatingActionButton.
    Scaffold(
        topBar = {
            // Kopfzeile der Ansicht mit dem Titel "Happy Places"
            TopAppBar(
                title = { Text("Happy Places") }
            )
        },
        floatingActionButton = {
            // Button zum Hinzufügen eines neuen Happy Place
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { innerPadding ->
        // Die eigentliche Liste der Orte wird hier angezeigt.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Für jeden Ort in der Liste wird ein ListItem angezeigt.
            items(happyPlaces) { place ->
                HappyPlaceListItem(
                    place = place,
                    onClick = { onPlaceClick(place) }
                )
            }
        }
    }
}

/**
 * Einzelnes Listenelement für einen Happy Place.
 *
 * @param place Der anzuzeigende Happy Place.
 * @param onClick Wird ausgelöst, wenn auf das Listenelement geklickt wird.
 */
@Composable
fun HappyPlaceListItem(
    place: HappyPlace,
    onClick: () -> Unit
) {
    // Card sorgt für ein optisch ansprechendes Listenelement
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        // Inhalt des Listenelements: Titel und Beschreibung
        Column(modifier = Modifier.padding(16.dp)) {
            // Titel des Ortes (z.B. Name des Happy Place)
            Text(text = place.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            // Kurzbeschreibung des Ortes
            Text(text = place.beschreibung, style = MaterialTheme.typography.bodyMedium)
        }
    }
}