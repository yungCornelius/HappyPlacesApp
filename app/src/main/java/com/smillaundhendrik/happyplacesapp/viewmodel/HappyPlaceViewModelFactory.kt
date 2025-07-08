package com.smillaundhendrik.happyplacesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smillaundhendrik.happyplacesapp.data.HappyPlaceRepository

/**
 * ViewModelFactory für das HappyPlaceViewModel.
 * Erlaubt die Übergabe des Repositories an das ViewModel.
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