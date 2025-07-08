package com.smillaundhendrik.happyplacesapp.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.content.pm.PackageManager
import androidx.annotation.RequiresPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHappyPlaceScreen(
    navController: NavController,
    tempLat: Double?,
    tempLon: Double?,
    onLocationReset: () -> Unit,
    onSave: (name: String, beschreibung: String, bildPfad: String, latitude: Double, longitude: Double, notizen: String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var beschreibung by rememberSaveable  { mutableStateOf("") }
    var notizen by rememberSaveable  { mutableStateOf("") }
    var bildPfad by rememberSaveable  { mutableStateOf("") }
    var error by rememberSaveable  { mutableStateOf<String?>(null) }

    // Die State-Variablen für die Koordinaten:
    var latitude by remember { mutableStateOf<Double?>(tempLat) }
    var longitude by remember { mutableStateOf<Double?>(tempLon) }

    // Übernehme neue Koordinaten, wenn tempLat/tempLon sich ändern (z.B. nach Kartenpick)
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name*") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = beschreibung,
                onValueChange = { beschreibung = it },
                label = { Text("Beschreibung") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Standort übernehmen
                LocationButton { lat, lon ->
                    latitude = lat
                    longitude = lon
                }
                // Karte wählen
                Button(onClick = { navController.navigate("pickLocation") }) {
                    Text("Auf Karte wählen")
                }
            }

            if (latitude != null && longitude != null) {
                Text("Gewählter Standort: $latitude, $longitude")
            } else {
                Text("Kein Standort gewählt", color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = notizen,
                onValueChange = { notizen = it },
                label = { Text("Notizen") },
                modifier = Modifier.fillMaxWidth()
            )
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Button(
                onClick = {
                    if (name.isBlank() || latitude == null || longitude == null) {
                        error = "Bitte Name und Standort angeben."
                    } else {
                        onSave(
                            name,
                            beschreibung,
                            bildPfad,
                            latitude!!,
                            longitude!!,
                            notizen
                        )
                        navController.popBackStack() // zurück zur Liste
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Speichern")
            }
        }
    }
}

// ---- Ab HIER NACH AddHappyPlaceScreen ----

@SuppressLint("MissingPermission")
@Composable
fun LocationButton(
    onLocationReceived: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    var error by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation(context, onLocationReceived, { error = it })
        } else {
            error = "Standortzugriff verweigert"
        }
    }

    Button(onClick = {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                getCurrentLocation(context, onLocationReceived, { error = it })
            }
            else -> {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }) {
        Text("Aktuellen Standort übernehmen")
    }
    error?.let {
        Text(it, color = MaterialTheme.colorScheme.error)
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getCurrentLocation(
    context: Context,
    onLocation: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            onLocation(location.latitude, location.longitude)
        } else {
            // Fallback: request new location
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000
            ).setMaxUpdates(1).build()
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        val loc = result.lastLocation
                        if (loc != null) {
                            onLocation(loc.latitude, loc.longitude)
                        } else {
                            onError("Standort konnte nicht ermittelt werden")
                        }
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                },
                Looper.getMainLooper()
            )
        }
    }.addOnFailureListener {
        onError("Standort konnte nicht ermittelt werden")
    }
}