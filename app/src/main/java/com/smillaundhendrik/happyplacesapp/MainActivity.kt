package com.smillaundhendrik.happyplacesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smillaundhendrik.happyplacesapp.data.HappyPlace
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceDatabase
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceRepository
import com.smillaundhendrik.happyplacesapp.screens.AddHappyPlaceScreen
import com.smillaundhendrik.happyplacesapp.screens.HappyPlaceListScreen
import com.smillaundhendrik.happyplacesapp.screens.PickLocationOnMapScreen
import com.smillaundhendrik.happyplacesapp.viewmodel.HappyPlaceViewModel
import com.smillaundhendrik.happyplacesapp.viewmodel.HappyPlaceViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Datenbank, Repository, ViewModel initialisieren
        val dao = HappyPlaceDatabase.getDatabase(applicationContext).happyPlaceDao()
        val repository = HappyPlaceRepository(dao)
        val viewModelFactory = HappyPlaceViewModelFactory(repository)
        val happyPlaceViewModel: HappyPlaceViewModel by viewModels { viewModelFactory }

        setContent {
            val navController = rememberNavController()
            var tempLat: Double? = null
            var tempLon: Double? = null

            // Datenbank-Flow als Compose-State
            val happyPlaces by happyPlaceViewModel.allHappyPlaces.collectAsState(initial = emptyList())

            NavHost(navController = navController, startDestination = "happyPlaceList") {
                composable("happyPlaceList") {
                    HappyPlaceListScreen(
                        happyPlaces = happyPlaces,
                        onAddClick = { navController.navigate("addHappyPlace") },
                        onDeleteClick = { place -> happyPlaceViewModel.delete(place) }
                    )
                }
                composable("addHappyPlace") {
                    AddHappyPlaceScreen(
                        navController = navController,
                        tempLat = tempLat,
                        tempLon = tempLon,
                        onLocationReset = { tempLat = null; tempLon = null },
                        onSave = { name, description, bildPfad, lat, lon, notizen ->
                            happyPlaceViewModel.insert(
                                HappyPlace(
                                    name = name,
                                    beschreibung = description,
                                    bildPfad = bildPfad,
                                    latitude = lat,
                                    longitude = lon,
                                    notizen = notizen
                                )
                            )
                            navController.popBackStack()
                        }
                    )
                }
                composable("pickLocation") {
                    PickLocationOnMapScreen(
                        onLocationPicked = { lat, lon ->
                            tempLat = lat
                            tempLon = lon
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}