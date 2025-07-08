package com.smillaundhendrik.happyplacesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smillaundhendrik.happyplacesapp.data.HappyPlace
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceRepository
import kotlinx.coroutines.launch

/**
 * ViewModel für die Happy Places App.
 * Liefert die Liste aller Orte als Flow und kapselt alle Datenbankoperationen.
 */
class HappyPlaceViewModel(private val repository: HappyPlaceRepository) : ViewModel() {

    // Flow mit allen gespeicherten HappyPlaces (wird in der UI beobachtet)
    // Hier wird angenommen, dass das Repository eine Property 'allHappyPlaces' bereitstellt.
    val allHappyPlaces = repository.allHappyPlaces

    /**
     * Einen neuen Ort einspeichern.
     * Die Methode startet einen Coroutine-Job im Hintergrund.
     */
    fun insert(happyPlace: HappyPlace) = viewModelScope.launch {
        repository.insert(happyPlace)
    }

    /**
     * Einen Ort aus der Datenbank löschen.
     * Die Methode startet einen Coroutine-Job im Hintergrund.
     */
    fun delete(happyPlace: HappyPlace) = viewModelScope.launch {
        repository.delete(happyPlace)
    }
}