package com.smillaundhendrik.happyplacesapp.screens

import android.content.Context
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

/**
 * Zeigt eine einfache OpenStreetMap-Karte mit osmdroid an.
 */
@Composable
fun OsmDroidMapScreen() {
    val context = LocalContext.current

    // Die Konfiguration von osmdroid braucht einen Kontext (ApplicationContext!)
    Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Standard-Zoom und Position (Berlin als Beispiel)
                controller.setZoom(13.0)
                controller.setCenter(GeoPoint(52.5200, 13.4050))
            }
        },
        update = { /* Hier kannst du später Marker oder Positionen updaten */ }
    )
}