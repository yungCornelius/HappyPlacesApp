package com.smillaundhendrik.happyplacesapp.screens

import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickLocationOnMapScreen(
    onLocationPicked: (latitude: Double, longitude: Double) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var pickedPoint by remember { mutableStateOf<GeoPoint?>(null) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Standort auf Karte wählen") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Die Karte
            AndroidView(
                factory = { ctx ->
                    val mapView = MapView(ctx).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0 // wird von Modifier.weight(1f) überschrieben
                        )
                        controller.setZoom(13.0)
                        controller.setCenter(GeoPoint(52.5200, 13.4050)) // Startpunkt Berlin
                    }

                    // Overlay für Tap-Events
                    val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            if (p != null) {
                                pickedPoint = p
                                // Alten Marker entfernen
                                mapView.overlays.removeAll { it is Marker }
                                // Neuen Marker setzen
                                Marker(mapView).apply {
                                    position = p
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    mapView.overlays.add(this)
                                }
                                mapView.controller.setCenter(p)
                                mapView.invalidate()
                            }
                            return true
                        }
                        override fun longPressHelper(p: GeoPoint?): Boolean = false
                    })
                    mapView.overlays.add(eventsOverlay)
                    mapView
                },
                update = { mapView ->
                    pickedPoint?.let { point ->
                        // Marker aktualisieren, falls nötig (Sicherheit)
                        mapView.overlays.removeAll { it is Marker }
                        Marker(mapView).apply {
                            position = point
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            mapView.overlays.add(this)
                        }
                        mapView.controller.setCenter(point)
                        mapView.invalidate()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onBack) {
                    Text("Abbrechen")
                }
                Button(
                    onClick = {
                        pickedPoint?.let { onLocationPicked(it.latitude, it.longitude) }
                    },
                    enabled = pickedPoint != null
                ) {
                    Text("Standort übernehmen")
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}