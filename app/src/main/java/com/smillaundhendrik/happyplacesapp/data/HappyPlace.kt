package com.smillaundhendrik.happyplacesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Datenmodell für einen gespeicherten Lieblingsort ("Happy Place").
 * Diese Klasse repräsentiert eine Entität in der Room-Datenbank.
 */
@Entity(tableName = "happy_places")
data class HappyPlace(
    /**
     * Eindeutige ID für jeden Ort (wird von Room automatisch vergeben).
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Name/Titel des Ortes (z.B. "Park am See").
     */
    val name: String,

    /**
     * Beschreibung des Ortes (optional, z.B. "Schöner Platz zum Entspannen").
     */
    val beschreibung: String,

    /**
     * Lokaler Pfad oder URI zum gespeicherten Bild (als String).
     */
    val bildPfad: String,

    /**
     * Geographische Breite des Ortes (Latitude).
     */
    val latitude: Double,

    /**
     * Geographische Länge des Ortes (Longitude).
     */
    val longitude: Double,

    /**
     * Persönliche Notizen zum Ort (optional).
     */
    val notizen: String
)