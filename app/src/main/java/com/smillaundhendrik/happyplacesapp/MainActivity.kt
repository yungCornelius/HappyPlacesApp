package com.smillaundhendrik.happyplacesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smillaundhendrik.happyplacesapp.screens.AddHappyPlaceScreen
import com.smillaundhendrik.happyplacesapp.screens.HappyPlaceListScreen
import com.smillaundhendrik.happyplacesapp.screens.PickLocationOnMapScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var tempLat by remember { mutableStateOf<Double?>(null) }
            var tempLon by remember { mutableStateOf<Double?>(null) }
            var happyPlaces by remember { mutableStateOf(listOf<HappyPlace>()) }

            NavHost(navController = navController, startDestination = "happyPlaceList") {
                composable("happyPlaceList") {
                    HappyPlaceListScreen(
                        happyPlaces = happyPlaces,
                        onAddClick = { navController.navigate("addHappyPlace") }
                    )
                }
                composable("addHappyPlace") {
                    AddHappyPlaceScreen(
                        navController = navController,
                        tempLat = tempLat,
                        tempLon = tempLon,
                        onLocationReset = { tempLat = null; tempLon = null },
                        onSave = { name, description, lat, lon ->
                            happyPlaces = happyPlaces + HappyPlace(name, description, lat, lon)
                        }
                    )
                }
                composable("pickLocation") {
                    PickLocationOnMapScreen(
                        onLocationPicked = { lat, lon ->
                            tempLat = lat
                            tempLon = lon
                            navController.popBackStack() // zurück zum Add-Screen
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}