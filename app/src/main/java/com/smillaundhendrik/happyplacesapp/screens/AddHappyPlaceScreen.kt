package com.smillaundhendrik.happyplacesapp.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Screen um einen neuen Happy Place anzulegen – mit Bildauswahl aus Galerie oder Kamera.
 *
 * Dieser Screen bietet ein Formular zum Eingeben aller Felder des Datenmodells HappyPlace:
 * - Name (Pflichtfeld)
 * - Beschreibung (optional)
 * - Bildauswahl (entweder aus Galerie oder per Kamera aufnehmen)
 * - Koordinaten (Latitude/Longitude)
 * - Notizen (optional)
 *
 * Das gewählte Bild wird als Vorschau angezeigt. Nach dem Speichern werden alle Werte an onSaveClick übergeben.
 *
 * @param onSaveClick Callback, wenn auf "Speichern" geklickt wurde (liefert alle Felder zurück)
 * @param onCancelClick Callback, falls der Nutzer abbricht (optional)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHappyPlaceScreen(
    onSaveClick: (
        name: String,
        beschreibung: String,
        bildPfad: String,
        latitude: Double,
        longitude: Double,
        notizen: String
    ) -> Unit,
    onCancelClick: (() -> Unit)? = null
) {
    // Zustand für alle Eingabefelder
    var name by remember { mutableStateOf("") }
    var beschreibung by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var notizen by remember { mutableStateOf("") }

    // Zustand für das gewählte Bild (als URI)
    var bildUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher für die Galerie (ActivityResult API)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        bildUri = uri
    }

    // Kamera-Launcher (Placeholder, muss noch gebaut werden)
    // val cameraLauncher = ...

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neuen Happy Place anlegen") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bildvorschau oder Platzhalter
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                if (bildUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(bildUri),
                        contentDescription = "Gewähltes Bild",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Kein Bild gewählt", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Button: Bild auswählen
            Button(onClick = { showDialog = true }) {
                Text("Bild auswählen")
            }

            // Dialog für Auswahl: Galerie oder Kamera
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {},
                    title = { Text("Bild hinzufügen") },
                    text = {
                        Column {
                            Button(onClick = {
                                showDialog = false
                                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }) {
                                Text("Aus Galerie wählen")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                showDialog = false
                                // TODO: Kamera-Logik (kann später ergänzt werden)
                            }) {
                                Text("Mit Kamera aufnehmen")
                            }
                        }
                    }
                )
            }

            // Name (Pflichtfeld)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name*") },
                singleLine = true
            )
            // Beschreibung
            OutlinedTextField(
                value = beschreibung,
                onValueChange = { beschreibung = it },
                label = { Text("Beschreibung") }
            )
            // Latitude
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Breitengrad") }
            )
            // Longitude
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Längengrad") }
            )
            // Notizen
            OutlinedTextField(
                value = notizen,
                onValueChange = { notizen = it },
                label = { Text("Notizen") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Speichern-Button
            Button(
                onClick = {
                    val lat = latitude.toDoubleOrNull() ?: 0.0
                    val lon = longitude.toDoubleOrNull() ?: 0.0
                    onSaveClick(
                        name,
                        beschreibung,
                        bildUri?.toString() ?: "",
                        lat,
                        lon,
                        notizen
                    )
                },
                enabled = name.isNotBlank()
            ) {
                Text("Speichern")
            }

            // Optional: Abbrechen-Button
            if (onCancelClick != null) {
                TextButton(onClick = onCancelClick) {
                    Text("Abbrechen")
                }
            }
        }
    }
}