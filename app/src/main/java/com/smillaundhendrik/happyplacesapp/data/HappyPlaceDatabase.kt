package com.smillaundhendrik.happyplacesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room-Datenbankklasse für die Happy Places App.
 * Hier werden alle Entities (Tabellen) und DAOs registriert.
 */
@Database(
    entities = [HappyPlace::class],
    version = 1,
    exportSchema = false
)
abstract class HappyPlaceDatabase : RoomDatabase() {

    /**
     * Zugriff auf das DAO für die Happy Places Tabelle.
     */
    abstract fun happyPlaceDao(): HappyPlaceDao

    companion object {
        // Volatile stellt sicher, dass Änderungen sofort für alle Threads sichtbar sind.
        @Volatile
        private var INSTANCE: HappyPlaceDatabase? = null

        /**
         * Gibt die Singleton-Instanz der Datenbank zurück oder erstellt sie, falls sie noch nicht existiert.
         */
        fun getDatabase(context: Context): HappyPlaceDatabase {
            // Doppelt-gesperrtes Singleton-Muster für Thread-Sicherheit.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HappyPlaceDatabase::class.java,
                    "happy_place_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}