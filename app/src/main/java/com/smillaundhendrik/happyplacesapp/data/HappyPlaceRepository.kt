package com.smillaundhendrik.happyplacesapp.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository für die HappyPlaces App.
 * Vermittelt zwischen Datenbank (DAO) und ViewModel/UI.
 */
class HappyPlaceRepository(private val dao: HappyPlaceDao) {

    /**
     * Gibt alle gespeicherten Orte als Flow zurück (wird automatisch aktualisiert).
     */
    val allHappyPlaces: Flow<List<HappyPlace>> = dao.getAllHappyPlaces()

    /**
     * Fügt einen neuen Ort ein.
     */
    suspend fun insert(happyPlace: HappyPlace) {
        dao.insert(happyPlace)
    }

    /**
     * Aktualisiert einen Ort.
     */
    suspend fun update(happyPlace: HappyPlace) {
        dao.update(happyPlace)
    }

    /**
     * Löscht einen Ort.
     */
    suspend fun delete(happyPlace: HappyPlace) {
        dao.delete(happyPlace)
    }

    /**
     * Holt einen Ort nach ID.
     */
    suspend fun getById(id: Int): HappyPlace? {
        return dao.getHappyPlaceById(id)
    }
}