package com.smillaundhendrik.happyplacesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smillaundhendrik.happyplacesapp.data.HappyPlace
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel für die Happy Places App.
 * Vermittelt zwischen UI und Repository/Datenbank.
 */
class HappyPlaceViewModel(private val repository: HappyPlaceRepository) : ViewModel() {

    /**
     * StateFlow, der die aktuelle Liste aller gespeicherten Orte hält.
     * Die UI kann diesen Flow beobachten und aktualisiert sich automatisch.
     */
    val allHappyPlaces: StateFlow<List<HappyPlace>> = repository.allHappyPlaces
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Neuen Ort einfügen.
     */
    fun insert(happyPlace: HappyPlace) {
        viewModelScope.launch {
            repository.insert(happyPlace)
        }
    }

    /**
     * Ort aktualisieren.
     */
    fun update(happyPlace: HappyPlace) {
        viewModelScope.launch {
            repository.update(happyPlace)
        }
    }

    /**
     * Ort löschen.
     */
    fun delete(happyPlace: HappyPlace) {
        viewModelScope.launch {
            repository.delete(happyPlace)
        }
    }

    /**
     * Einzelnen Ort nach ID holen (Suspending Function).
     */
    suspend fun getById(id: Int): HappyPlace? {
        return repository.getById(id)
    }
}

/**
 * Factory zum Erzeugen des ViewModels mit Repository-Parameter.
 */
class HappyPlaceViewModelFactory(
    private val repository: HappyPlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HappyPlaceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HappyPlaceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}