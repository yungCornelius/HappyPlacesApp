package com.smillaundhendrik.happyplacesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.collectAsState
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceDatabase
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceRepository
import com.smillaundhendrik.happyplacesapp.viewmodel.HappyPlaceViewModel
import com.smillaundhendrik.happyplacesapp.viewmodel.HappyPlaceViewModelFactory // Korrekt importieren!
import com.smillaundhendrik.happyplacesapp.screens.HappyPlaceListScreen
import com.smillaundhendrik.happyplacesapp.screens.AddHappyPlaceScreen
import com.smillaundhendrik.happyplacesapp.ui.theme.HappyPlacesAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: HappyPlaceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewModel initialisieren – Factory wird verwendet, damit das ViewModel das Repository kennt
        val dao = HappyPlaceDatabase.getDatabase(applicationContext).happyPlaceDao()
        val repository = HappyPlaceRepository(dao)
        val factory = HappyPlaceViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HappyPlaceViewModel::class.java]

        setContent {
            // Die aktuelle Liste aller gespeicherten Orte beobachten (wird bei Änderung der Datenbank automatisch aktualisiert)
            val happyPlaces by viewModel.allHappyPlaces.collectAsState(initial = emptyList())

            // Navigation Controller für Compose
            val navController = rememberNavController()

            // Theme übernimmt das Systemverhalten (Dark/Light Mode)
            HappyPlacesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    // Floating Action Button (FAB) unten rechts für "Add"
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navController.navigate("add") }
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Neuen Happy Place hinzufügen")
                        }
                    }
                ) { innerPadding ->
                    // Navigation Host für die Screens
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Screen: Liste aller Orte, mit Delete-Callback für Swipe-to-Delete
                        composable("list") {
                            HappyPlaceListScreen(
                                happyPlaces = happyPlaces,
                                onAddClick = { navController.navigate("add") },
                                onPlaceClick = { /* TODO: Detail-Screen-Navigation */ },
                                onDeleteClick = { place -> viewModel.delete(place) }
                            )
                        }
                        // Screen: Neuen Ort anlegen
                        composable("add") {
                            AddHappyPlaceScreen(
                                onSaveClick = { name: String, beschreibung: String, bildPfad: String, latitude: Double, longitude: Double, notizen: String ->
                                    viewModel.insert(
                                        com.smillaundhendrik.happyplacesapp.data.HappyPlace(
                                            name = name,
                                            beschreibung = beschreibung,
                                            bildPfad = bildPfad,
                                            latitude = latitude,
                                            longitude = longitude,
                                            notizen = notizen
                                        )
                                    )
                                    navController.popBackStack()
                                },
                                onCancelClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}