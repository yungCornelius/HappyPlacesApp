package com.smillaundhendrik.happyplacesapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) für die HappyPlaces-Datenbank.
 * Hier werden alle Methoden definiert, mit denen auf die Datenbank zugegriffen wird.
 */
@Dao
interface HappyPlaceDao {

    /**
     * Einen neuen Ort in die Datenbank einfügen.
     * Bei Konflikt (z.B. gleicher Primärschlüssel) wird der neue Eintrag überschrieben.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(happyPlace: HappyPlace)

    /**
     * Einen Ort aktualisieren.
     */
    @Update
    suspend fun update(happyPlace: HappyPlace)

    /**
     * Einen Ort löschen.
     */
    @Delete
    suspend fun delete(happyPlace: HappyPlace)

    /**
     * Alle gespeicherten Orte als Liste zurückgeben (automatisch aktualisiert, wenn sich die Daten ändern).
     */
    @Query("SELECT * FROM happy_places ORDER BY id DESC")
    fun getAllHappyPlaces(): Flow<List<HappyPlace>>

    /**
     * Einen bestimmten Ort anhand der ID laden.
     */
    @Query("SELECT * FROM happy_places WHERE id = :id")
    suspend fun getHappyPlaceById(id: Int): HappyPlace?
}